package com.github.redreaperlp.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private JSONObject user;
    private int id;

    public User(int id) {
        this.id = id;
        this.user = new JSONObject();
    }

    public void set(String key, Object value) {
        try {
            user.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject get() {
        return user;
    }

    public int id() {
        return id;
    }

    public String stringID () {
        return String.valueOf(id);
    }
}
