package com.github.redreaperlp.networking;

import java.util.List;

public class TaskRegister {
    public static List<String> taskIDs;

    public static String createTaskID() {
        String taskID = "";
        for (int i = 0; i < 10; i++) {
            taskID += (int) (Math.random() * 10);
        }
        if (taskIDs.contains(taskID)) {
            return createTaskID();
        } else {
            taskIDs.add(taskID);
            return taskID;
        }
    }

    public static void removeTaskID(String taskID) {
        taskIDs.remove(taskID);
    }

    public static boolean containsTaskID(String taskID) {
        return taskIDs.contains(taskID);
    }
}
