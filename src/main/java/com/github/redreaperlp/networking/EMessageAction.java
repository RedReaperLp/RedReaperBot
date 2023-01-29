package com.github.redreaperlp.networking;

public enum EMessageAction {
    AUTHENTICATE(1),
    PORT(2),
    TASK_KEY(10),
    PING(33),
    PONG(34),

    CONTROL_ID(35), // [1] = Start, [2] = Stop, [3] = Log, [4] = Status, [5] = Restart
    INTERACTION_OPTIONS(50),    // 50-59

    ANSWER(100),
    ERROR(404),
    ACCESS(800)

    ;

    EMessageAction(int id) {
        this.id = id;
    }

    public EMessageAction fromId(int id) {
        for (EMessageAction action : values()) {
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
