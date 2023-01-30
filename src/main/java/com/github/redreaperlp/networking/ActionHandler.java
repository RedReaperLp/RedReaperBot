package com.github.redreaperlp.networking;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Iterator;

public class ActionHandler implements Runnable {
    private final Action action;

    public ActionHandler(Action action) {
        this.action = action;
        new Thread(this).start();
    }

    @Override
    public void run() {
        String auth = action.get(EMessageAction.AUTHENTICATE);
        String ping = action.get(EMessageAction.PING);
        String pong = action.get(EMessageAction.PONG);
        String answer = action.get(EMessageAction.ANSWER);
        String interactOptions = action.get(EMessageAction.INTERACTION_OPTIONS);
        EMessageAction.PONG.value(ping);
        EMessageAction.AUTHENTICATE.value(auth);
        String portString = action.get(EMessageAction.PORT);
        int port = Integer.parseInt(portString);
        if (auth != null) {
            EMessageAction.ACCESS.value("404");
            if (!RedReaperBot.authTokens.getToken(auth)) {
                send(port, Codec.encode(EMessageAction.ACCESS, EMessageAction.PONG));
            }
        } else return;
        if (pong != null) {
            TaskRegister.removeTaskID(pong);
            if (answer != null) {
                AnswerUtil.addAnswer(pong, answer);
            }
            if (TaskRegister.containsTaskID(pong)) {
            }



            return;
        }
        if (!TaskRegister.containsDoneTaskID(ping)) {
            if (interactOptions != null) {
                try {
                    JSONObject options = new JSONObject(interactOptions);
                    for (Iterator it = options.keys(); it.hasNext(); ) {
                        String key = (String) it.next();
                        JSONObject option = options.getJSONObject(key);
                        String guildID = RedReaperBot.authTokens.getGuildID(auth);
                        JSONObject guild = RedReaperBot.servers.getGuild(guildID);
                        if (!guild.has(JsonSpecifier.CONTROL.key())) {
                            guild.put(JsonSpecifier.CONTROL.key(), new JSONObject());
                        }
                        JSONObject control = guild.getJSONObject(JsonSpecifier.CONTROL.key());
                        control.put(key, option.put(JsonSpecifier.TARGET_IP.key(), action.IP().getHostName() + ":" + portString));

                        RedReaperBot.servers.changes();
                    }
                } catch (JSONException e) {
                    EMessageAction.ERROR.value("Invalid JSON");
                }
                send(port, Codec.encode(EMessageAction.ACCESS, EMessageAction.PONG, EMessageAction.ERROR));
                return;
            }
        }
        EMessageAction.ACCESS.value("200");
        send(port, Codec.encode(EMessageAction.PONG, EMessageAction.AUTHENTICATE, EMessageAction.ACCESS));
    }

    public void send(int port, String message) {
        Socket socket = null;
        try {
            System.out.println("Received connection from " + action.IP() + ":" + port);
            socket = new Socket(action.IP(), port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message);
            writer.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
