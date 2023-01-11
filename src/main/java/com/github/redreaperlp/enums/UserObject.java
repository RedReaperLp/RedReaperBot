package com.github.redreaperlp.enums;

public enum UserObject {
    STATS("stats"),
    STATS_CHATPOINT("chatpoints"),
    NAME("name");


    private String key;

    UserObject(String name) {
        this.key = name;
    }

    public String key() {
        return key;
    }

}
