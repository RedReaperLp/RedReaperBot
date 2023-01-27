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
        String auth = action.get(MessageAction.AUTHENTICATE);
        String ping = action.get(MessageAction.PING);
        String pong = action.get(MessageAction.PONG);
        String interactOptions = action.get(MessageAction.INTERACTION_OPTIONS);
        MessageAction.PONG.value(ping);
        MessageAction.AUTHENTICATE.value(auth);
        if (auth != null) {

        } else return;
        int port = Integer.parseInt(action.get(MessageAction.PORT));

        if (interactOptions != null) {
            try {
                JSONObject options = new JSONObject(interactOptions);
                for (Iterator it = options.keys(); it.hasNext(); ) {
                    String key = (String) it.next();
                    JSONObject option = options.getJSONObject(key);
                    System.out.println(key);
                    String guild = option.getString(JsonSpecifier.GUILD.key());
                    if (RedReaperBot.servers.storageObj.getJSONObject(JsonSpecifier.STORAGE.key()).getJSONObject(JsonSpecifier.GUILD.key()).has(guild)) {
                        JSONObject guildObj = RedReaperBot.servers.getGuild(guild);
                        if (guildObj.has(JsonSpecifier.CONTROL.key())) {
                            guildObj.getJSONObject(JsonSpecifier.CONTROL.key()).put(key, option);
                        } else {
                            guildObj.put(JsonSpecifier.CONTROL.key(), new JSONObject().put(key, option));
                        }
                        RedReaperBot.servers.changes();
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        Socket socket = null;
        try {
            socket = new Socket(action.IP(), port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(Codec.encode(MessageAction.PONG, MessageAction.AUTHENTICATE));
            writer.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
