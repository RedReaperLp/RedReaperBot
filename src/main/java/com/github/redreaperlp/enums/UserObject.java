package com.github.redreaperlp.enums;

import org.json.JSONObject;

public enum UserObject {
    STATS(CommandEn.STATS.key()),
    STATS_LEVEL("level"),
    STATS_CHATPOINT(CommandEn.CHATPOINTS.key()),
    NAME("name");



    private String key;
    private int chatpoints;
    private JSONObject value;
    private String uName;

    UserObject(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    public int chatpoints() {
        return chatpoints;
    }

    public void chatpoints(int id) {
        this.chatpoints = id;
    }

    public JSONObject value() {
        return value;
    }

    public UserObject value(JSONObject value) {
        this.value = value;
        return this;
    }

    public String uName() {
        return uName;
    }

    public void uName(String uName) {
        this.uName = uName;
    }

}
