package by.bsu.up;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dasha on 13.05.2016.
 */
public class Item {
    private String username;
    private String hashPassword;
    private String id;

    public String getUsername() { return username; }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }



}
