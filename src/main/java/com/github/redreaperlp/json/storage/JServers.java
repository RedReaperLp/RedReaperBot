package com.github.redreaperlp.json.storage;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.user.JUser;
import com.github.redreaperlp.json.storage.user.stats.JStats;
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

    private final JUser user = new JUser();

    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

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
            checkContain();
            if (!storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).has(guild.getId())) {
                storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).put(guild.getId(), new JSONObject()
                        .put(USERS.key(), new JSONObject()));
                changes();
            } else {
                storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).getJSONObject(guild.getId());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void addGuild(String guildID) {
        try {
            checkContain();
            if (!storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).has(guildID)) {
                storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).put(guildID, new JSONObject()
                        .put(USERS.key(), new JSONObject()));
                changes();
            } else {
                storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).getJSONObject(guildID);
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
            try {
                return storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).put(guild.getId(), new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public JSONObject getGuild(String guildID) {
        addGuild(guildID);
        try {
            return storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).getJSONObject(guildID);
        } catch (JSONException e) {
            try {
                return storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).put(guildID, new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void removeGuild(String id) {
        try {
            storageObj.getJSONObject(STORAGE.key()).getJSONObject(GUILD.key()).remove(id);
            changes();
        } catch (JSONException e) {

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
        return user.stats();
    }


    public void changes() {
        JsonHelper.serversChange();
    }
}

