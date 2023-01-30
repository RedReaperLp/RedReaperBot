package com.github.redreaperlp.networking;

import com.github.redreaperlp.RedReaperBot;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Sender implements Runnable {

    private String targetIp;
    private int port;
    private String sendable;
    private ButtonInteractionEvent e;
    private String taskID;

    public Sender(String targetIp, int port, String sendable, ButtonInteractionEvent e) {
        this.taskID = TaskRegister.createTaskID();
        this.targetIp = targetIp;
        this.sendable = sendable;
        this.port = port;
        this.e = e;

        int counter = 1;
        while (TaskRegister.containsTaskID(taskID)) {
            if (counter > 5) {
                TaskRegister.removeTaskID(taskID);
                break;
            }

            try {
                counter++;
                new Thread(this).start();
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            AnswerAssosiation answerAssosiation = AnswerUtil.getAnswer(taskID);
            if (answerAssosiation != null) {
                String[] answerArray = answerAssosiation.getAnswer().split("-", 2);
                answerArray[1] = answerArray[1].substring(0, answerArray[1].length() - 1);
                int type = Integer.parseInt(answerArray[0]);
                String answer = answerArray[1];
                switch (type) {
                    case 1:
                        e.getHook().sendMessage(answer.replace("%line", "\n")).queue();
                        break;
                    case 2:
                        answer = answer.replaceFirst("%line", "").replace("%line", "\n");
                        e.getHook().sendFiles(FileUpload.fromData(answer.getBytes(), "Log.txt")).complete();
                        break;
                }


                AnswerUtil.removeAnswer(taskID);
            } else {
                e.getHook().sendMessage("No answer received").queue();
            }
        } catch (Exception ex) {
            e.getHook().sendMessage("No answer received").queue();
        }
    }

    @Override
    public void run() {

        try {
            EMessageAction.PING.value(taskID);
            EMessageAction.AUTHENTICATE.value(RedReaperBot.authTokens.getToken(e.getGuild()));
            sendable += "#" + Codec.encode(EMessageAction.PING, EMessageAction.AUTHENTICATE) + "=" + taskID;

            Socket socket = new Socket(targetIp, port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(sendable);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
