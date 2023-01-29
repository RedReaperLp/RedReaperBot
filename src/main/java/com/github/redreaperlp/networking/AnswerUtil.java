package com.github.redreaperlp.networking;

import java.util.ArrayList;
import java.util.List;

public class AnswerUtil {
    public static List<AnswerAssosiation> answerAssosiations = new ArrayList<>();
    public static void addAnswer(String taskID, String answer) {
        answerAssosiations.add(new AnswerAssosiation(taskID, answer));
    }

    public static AnswerAssosiation getAnswer(String taskID) {
        for (AnswerAssosiation answerAssosiation : answerAssosiations) {
            if (answerAssosiation.getTaskID().equals(taskID)) {
                return answerAssosiation;
            }
        }
        return null;
    }

    public static void removeAnswer(String taskID) {
        answerAssosiations.remove(getAnswer(taskID));
    }
}
