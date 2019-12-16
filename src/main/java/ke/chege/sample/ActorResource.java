/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.chege.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author chege
 */
@Path("actor")
@Produces({"application/json"})
public class ActorResource {

    @Resource(lookup = "java:global/actorDS")
    private DataSource ds;

    @Inject
    HttpServletRequest req;

    @GET
    public List<Actor> listActors() throws SQLException {
        Integer page = Integer.valueOf(req.getParameter("page"));
        Integer totalPerPage = 25;
        List<Actor> actors = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement("Select * from actor offset ? limit ?")) {
                statement.setObject(1, page*totalPerPage);
                statement.setObject(2, totalPerPage);
                statement.execute();
                try (ResultSet rs = statement.getResultSet()) {
                    while (rs.next()) {
                        actors.add(new Actor(rs.getInt("actor_id"), rs.getString("first_name"), rs.getString("last_name")));
                    }
                }
            }
        }
        return actors;
    }

}
