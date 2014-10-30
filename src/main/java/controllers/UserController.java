package controllers;

import models.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
@ApplicationScoped
public class UserController {

    private List<User> users = new ArrayList<User>();

    @Path("/all")
    @GET
    @Produces("application/json")
    public JsonArray getAll() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (User user : users) {
            builder.add(user.getJson());
        }
        return builder.build();
    }

    @Path("/{id}")
    @GET
    @Produces("application/json")
    public JsonObject getUser(@Context UriInfo uriInfo) {
        String stringID = uriInfo.getPathParameters().get("id").get(0);
        int index = new Integer(stringID);
        return (users.get(0).getJson());
    }

    private User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id)
                return (user);
        }
        return null;
    }

    @POST
    @Consumes("application/json")
    public JsonObject addUser(JsonObject newUser) {
        String name = newUser.getString("name");
        String email = newUser.getString("email");
        User user = new User(name, email);
        users.add(user);

        return (newUser);
    }

    @PostConstruct
    private void initUsers() {
        users.add(new User("John Smith", "jsmith123@xyzonline.com"));
        users.add(new User("Michael Jones", "m.jones@bckhd.com"));
    }

}
