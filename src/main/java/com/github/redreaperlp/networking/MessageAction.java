package com.github.redreaperlp.networking;

public enum MessageAction {
    AUTHENTICATE(1),
    PORT(2),
    TASK_KEY(10),
    PING(33),
    PONG(34),

    INTERACTION_OPTIONS(50),    // 50-59

    ;

    MessageAction(int id) {
        this.id = id;
    }

    public MessageAction fromId(int id) {
        for (MessageAction action : values()) {
            if (action.id() == id) {
                return action;
            }
        }
        return null;
    }

    private final int id;
    private String value;

    public int id() {
        return id;
    }

    public String value() {
        return value;
    }

    public void value(String value) {
        this.value = value;
    }

}
