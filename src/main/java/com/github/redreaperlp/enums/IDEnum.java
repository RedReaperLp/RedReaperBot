package com.github.redreaperlp.enums;

public enum IDEnum {

    USER("User", "user"),
    GUILD("Guild", "guild"),
    CHANNEL("Channel", "channel"),
    MESSAGE("Message",  "message"),
    ROLES("Roles", "roles"),

    BAN_BADWORD("banBadword", "banBadword"),
    KEEP_BADWORD("keepBadword", "keepBadword"),
    DELETE_BADWORD("deleteBadword", "deleteBadword"),

    WORD("word", "word"),



    ;

    private final String key;
    private final String id;

    public String key() {
        return key;
    }
    public String id() {
        return id;
    }

    IDEnum(String key, String id) {
        this.key = key;
        this.id = id;
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
