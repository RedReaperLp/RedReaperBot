package com.github.redreaperlp.json.storage.messages;

public enum AssosiationsEn {
    DELETER_MESSAGE("deleterMsg"),
    DELETER_MESSAGE_CHANNEL("deleterMsgChannel"),
    DELETE_TARGET("deletTarget"),
    DELETE_TARGET_CHANNEL("deleteTargetChannel"),
    CHANNEL_STATUS("channelStatus")


    ;

    private final String key;

    AssosiationsEn(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
