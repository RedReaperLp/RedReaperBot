package com.github.redreaperlp.enums;

import org.json.JSONObject;

public enum JsonSpecifier {
    STATS(CommandEn.STATS.key()),
    STATS_CHATPOINTS_LEVEL("level"),
    STATS_CHATPOINTS_POINTS(CommandEn.CHATPOINTS.key()),

    NAME("name"),

    GUILD("gld"),
    USERS("usr"),

    STORAGE("storage"),

    CHANNEL_CONFIG("chanConf"),

    MESSAGE_ASSOCIATION("msgAssos"),
    BAD_MESSAGES("bMsg"),


    CONTROL("control"),


    ID("id"), // Layer 1

    PROGRAM("program"), // Layer 2

    PATH("path"), // Layer 2

    PERMISSIONS("perms"), // Layer 2
    PERMISSION_ROLES("rls"), // Layer 3
    PERMISSION_USERS("usr"), // Layer 3
    TARGET_IP("tgIP"), // Layer 3)


    ;



    private final String key;
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
