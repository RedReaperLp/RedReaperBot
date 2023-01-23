package com.github.redreaperlp.enums;

import org.json.JSONObject;

public enum JsonSpecifier {
    STATS(CommandEn.STATS.key()),
    STATS_CHATPOINTS_LEVEL("level"),
    STATS_CHATPOINTS_POINTS(CommandEn.CHATPOINTS.key()),

    NAME("name"),

    GUILD("servers"),
    USERS("users"),

    STORAGE("storage"),

    CHANNEL_CONFIG("channelConfig"),

    MESSAGE_ASSOCIATION("messageAssociation"),




    ;



    private String key;
    private JSONObject defaultValue;

    JsonSpecifier(String key) {
        this.key = key;
    }

    public void defaultValue(JSONObject defaultValue) {
        this.defaultValue = defaultValue;
    }

    public JSONObject defaultValue() {
        return defaultValue;
    }
    public String key() {
        return key;
    }

}
