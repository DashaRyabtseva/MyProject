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

    public Item(String s1, String s2) {
        username = s1;
        hashPassword = s2;
    }
    public Item() {}

    public String getUsername() {
        return username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

}
