package com.github.redreaperlp.networking;

public class AnswerAssosiation {
    private String taskID;
    private String answer;

    public AnswerAssosiation(String taskID, String answer) {
        this.taskID = taskID;
        this.answer = answer;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getAnswer() {
        return answer;
    }
}
