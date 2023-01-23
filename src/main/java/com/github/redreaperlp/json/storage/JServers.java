package com.github.redreaperlp.json.storage;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.channel.JChannelConfigurations;
import com.github.redreaperlp.json.storage.messages.JMessageAssociation;
import com.github.redreaperlp.json.storage.user.JUser;
import com.github.redreaperlp.json.storage.user.stats.JStats;
import com.github.redreaperlp.json.storage.user.stats.util.JChatPoints;
import com.github.redreaperlp.util.JsonHelper;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class JServers {
    public static File file = new File(RedReaperBot.conf.getConfig(STORAGE.key()));

    public JSONObject storageObj = new JSONObject();
    private JChannelConfigurations channelConfigurations = new JChannelConfigurations();
    private JMessageAssociation messageAssociation = new JMessageAssociation();
    private JUser user = new JUser();

    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";
    public boolean changes = false;

    public JServers() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                checkContain();
                finalizer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            storageObj = JsonHelper.resolver(file);
            System.out.println(storageObj.toString());
        }
    }


    public List<Integer> getGuildIDs() {
        List<Integer> ids = new ArrayList<>();
        Iterator<String> keys = storageObj.keys();
        while (keys.hasNext()) {
            ids.add(Integer.parseInt(keys.next()));
        }
        return ids;
    }

    /**
     * Adds a Server to Config, if it isn´t already
     *
     * @param guild The Guild to add
     */
    public void addGuild(Guild guild) {
        try {
            if (!storageObj.has(STORAGE.key())) {
                storageObj.put(STORAGE.key(), new JSONObject());
            }
            if (!storageObj.getJSONObject(STORAGE.key()).has(GUILD.key())) {
                storageObj.getJSONObject(STORAGE.key()).put(GUILD.key(), new JSONObject());
            }
            JSONObject guildObj;
            if (!storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).has(guild.getId())) {
                guildObj = storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).put(guild.getId(), new JSONObject()
                        .put(USERS.key(), new JSONObject()));
                changes = true;
            } else {
                guildObj = storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).getJSONObject(guild.getId());
            }
            if (!guildObj.has(CHANNEL_CONFIG.key())) {
                guildObj.put(CHANNEL_CONFIG.key(), new JSONObject());
                changes();
            }
            if (!guildObj.has(MESSAGE_ASSOCIATION.key())) {
                guildObj.put(MESSAGE_ASSOCIATION.key(), new JSONObject());
                changes();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getGuild(Guild guild) {
        addGuild(guild);
        try {
            return storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).getJSONObject(guild.getId());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public void removeGuild(String id) {
        try {
            storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).remove(id);
            changes = true;
        } catch (JSONException e) {
            System.out.println("Error while removing Server: " + id);
        }
    }

    public void checkContain() {
        try {
            if (!storageObj.has(STORAGE.key())) {
                storageObj.put(STORAGE.key(), new JSONObject());
                changes();
            }
            if (!storageObj.getJSONObject(STORAGE.key()).has(GUILD.key())) {
                storageObj.getJSONObject(STORAGE.key()).put(GUILD.key(), new JSONObject());
                changes();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void finalizer() {
        JsonHelper.serverFinalizer(file, storageObj);
    }

    public JUser user() {
        return user;
    }

    public JStats stats() {
        return user().stats();
    }

    public JChatPoints chatPoints() {
        return stats().chatPoints();
    }

    public void changes() {
        JsonHelper.serversChange();
    }

    public JChannelConfigurations channelConfigurations() {
        return channelConfigurations;
    }

    public JMessageAssociation messageAssociation() {
        return messageAssociation;
    }
}

