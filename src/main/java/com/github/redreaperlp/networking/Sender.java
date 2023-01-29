package com.github.redreaperlp.networking;

import com.github.redreaperlp.RedReaperBot;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Sender implements Runnable{

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
            System.out.println(counter);
            if (counter > 5) {
                TaskRegister.removeTaskID(taskID);
                break;
            }

            try {
                counter++;
                new Thread(this).start();
                System.out.println("Sent Message");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        AnswerAssosiation answerAssosiation = AnswerUtil.getAnswer(taskID);
        if (answerAssosiation != null) {
            e.getHook().sendMessage(answerAssosiation.getAnswer()).queue();
            AnswerUtil.removeAnswer(taskID);
        } else {
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
