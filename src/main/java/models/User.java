package models;

import javax.json.Json;
import javax.json.JsonObject;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.Random;

public class User {
    private String name;
    private String email;
    private int id;

    public User(String name, String email) {

        this.name = name;
        this.email = email;
        this.id = generateId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int generateId() {
        long l = System.currentTimeMillis() * (new Random()).nextInt();
        String asString = "" + l;
        String as5String = asString.substring((asString.length() - 5), (asString.length()));
        return new Integer(as5String);
    }

    public JsonObject getJson() {
        return (Json.createObjectBuilder()
                    .add("id", id)
                    .add("name", name)
                    .add("email", email)
                    .build());
    }
}
