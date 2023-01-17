package com.github.redreaperlp.enums;

public enum JsonSpecifier {
    STATS(CommandEn.STATS.key()),
    STATS_CHATPOINTS_LEVEL("level"),
    STATS_CHATPOINTS_POINTS(CommandEn.CHATPOINTS.key()),
    NAME("name"),

    SERVERS("servers"),
    USERS("users"),

    STORAGE("storage"),;



    private String key;
    private int chatpoints;

    JsonSpecifier(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
