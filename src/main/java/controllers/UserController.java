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

    //Returns an array of user objects as Array of JSON
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

    //Returns a single user object as JSON
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public JsonObject getUser(@Context UriInfo uriInfo) {
        String stringID = uriInfo.getPathParameters().get("id").get(0);
        int index = new Integer(stringID);
        return (getUser(index).getJson());
    }

    private User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id)
                return (user);
        }
        return null;
    }

    //Create a new user from JSON
    @POST
    @Consumes("application/json")
    public JsonObject addUser(JsonObject newUser) {
        String name = newUser.getString("name");
        String email = newUser.getString("email");
        User user = new User(name, email);
        users.add(user);

        return (newUser);
    }

    //Update a user from JSON, return JSON
    @Path("/{id}")
    @PUT
    @Consumes("application/json")
    public JsonObject editUser(@Context UriInfo uriInfo, JsonObject newUserInfo) {
        String stringID = uriInfo.getPathParameters().get("id").get(0);
        int index = new Integer(stringID);
        User user = getUser(index);
        user.setName(newUserInfo.getString("name"));
        user.setEmail(newUserInfo.getString("email"));
        return (getUser(index).getJson());
    }

    //Delete a user by Id
    @Path("/{id}")
    @DELETE
    public void deleteUser(@Context UriInfo uriInfo) {
        int index = new Integer(uriInfo.getPathParameters().get("id").get(0));
        User user = getUser(index);
        users.remove(user);
    }

    @PostConstruct
    private void initUsers() {
        users.add(new User("John Smith", "jsmith123@xyzonline.com"));
        users.add(new User("Michael Jones", "m.jones@bckhd.com"));
    }

}
