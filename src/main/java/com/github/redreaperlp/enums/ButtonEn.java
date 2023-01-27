package com.github.redreaperlp.enums;

public enum ButtonEn {
    ROLES("Roles"),
    KEEP_MESSAGE("Keep Message"),
    DELETE_MESSAGE("Delete Message"),
    ;

    private final String key;

    ButtonEn(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
