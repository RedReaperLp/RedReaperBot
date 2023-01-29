package com.github.redreaperlp.networking;

public enum AnswerType {
    MESSAGE(1),
    FILE(2),

    ;

    private final int id;
    AnswerType(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
