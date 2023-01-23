package com.github.redreaperlp.enums;

public enum IDEnum {

    USER("user"),
    GUILD("guild"),
    CHANNEL("channel"),
    MESSAGE("message"),
    ROLES("roles"),

    BAN_BADWORD("banBadword"),
    KEEP_BADWORD("keepBadword"),
    DELETE_BADWORD("deleteBadword"),

    WORD("word"),



    ;

    private String key;

    public String key() {
        return key;
    }

    IDEnum(String key) {
        this.key = key;
    }

    public static IDEnum fromKey(String key) {
        for (IDEnum idEnum : values()) {
            if (idEnum.key().equals(key)) {
                return idEnum;
            }
        }
        return null;
    }
}
