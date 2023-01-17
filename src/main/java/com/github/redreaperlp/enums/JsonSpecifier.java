package com.github.redreaperlp.enums;

import org.json.JSONObject;

public enum JsonSpecifier {
    STATS(CommandEn.STATS.key()),
    STATS_CHATPOINTS_LEVEL("level"),
    STATS_CHATPOINT_POINTS(CommandEn.CHATPOINTS.key()),
    NAME("name"),

    SERVERS("servers"),
    USERS("users");



    private String key;
    private int chatpoints;
    private JSONObject value;
    private String uName;

    JsonSpecifier(String key) {
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

    public JsonSpecifier value(JSONObject value) {
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
