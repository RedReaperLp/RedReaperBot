package com.github.redreaperlp.enums;

import org.json.JSONObject;

public enum JsonSpecifier {
    STATS(CommandEn.STATS.key()),
    STATS_CHATPOINTS_LEVEL("level"),
    STATS_CHATPOINTS_POINTS(CommandEn.CHATPOINTS.key()),

    NAME("name"),

    GUILD("guild"),
    USERS("users"),

    STORAGE("storage"),

    CHANNEL_CONFIG("channelConfig"),

    MESSAGE_ASSOCIATION("messageAssociation"),
    BAD_MESSAGES("badMessages"),


    CONTROL("control"),

    CONTROLLABLE("controllable"),



    ID("id"), // Layer 1

    PROGRAM("program"), // Layer 2

    PATH("path"), // Layer 2

    PERMISSIONS("permissions"), // Layer 2
    ROLES("roles"), // Layer 3
    PERMISSION_USERS("users"), // Layer 3

    SCREEN("screen"), // Layer 2
    SCREEN_NAME("name"), // Layer 3
    STOP("stop"), // Layer 3
    START("start"), // Layer 3
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
