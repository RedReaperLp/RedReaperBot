package com.github.redreaperlp.enums;

public enum ConfEnum {
    TOKEN("token", "The token of the bot"),
    PLAYING("playing", "The playing status of the bot"),
    STORAGE("storage", "storage.json");
    private String key;
    private String defaultValue;

    ConfEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String key() {
        return key;
    }

    public String defaultValue() {
        return defaultValue;
    }
}
