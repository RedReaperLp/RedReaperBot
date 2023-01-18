package com.github.redreaperlp.util.storage.servers;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class Servers {
    public static File file = new File(Main.conf.getConfig(STORAGE.key()));

    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";
    public static JSONObject storageObj = new JSONObject();
    public static boolean changes = false;

    public Servers() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                storageObj.put(SERVERS.key(), new JSONObject());
                changes = true;
                checkContain();
                finalizer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            resolver();
        }
    }

    public void finalizer() {
        try {
            if (changes) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(storageObj.toString(4));
                writer.flush();
                writer.close();
                changes = false;
                System.out.println("Saved changes to storage.json");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void changes() {
        changes = true;
    }

    /**
     * Resolves the StorageFile and returns it to the local variable #servers
     *
     * @see #storageObj
     */
    public void resolver() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String lines = "";
            while ((line = reader.readLine()) != null) {
                lines += line;
            }
            if (lines.equals("")) {
                storageObj = new JSONObject();
            } else {
                storageObj = new JSONObject(lines);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getServerIDs() {
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
            if (!storageObj.getJSONObject(STORAGE.key()).has(SERVERS.key())) {
                storageObj.getJSONObject(STORAGE.key()).put(SERVERS.key(), new JSONObject());
            }
            if (!storageObj.getJSONObject(STORAGE.key()).getJSONObject(SERVERS.key()).has(guild.getId())) {
                storageObj.getJSONObject(STORAGE.key()).getJSONObject(SERVERS.key()).put(guild.getId(), new JSONObject()
                        .put(USERS.key(), new JSONObject()));
                changes = true;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getGuild(Guild guild) {
        addGuild(guild);
        try {
            return storageObj.getJSONObject(STORAGE.key()).getJSONObject(SERVERS.key()).getJSONObject(guild.getId());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public void removeServer(String id) {
        try {
            storageObj.getJSONObject(STORAGE.key()).getJSONObject(SERVERS.key()).remove(id);
            changes = true;
        } catch (JSONException e) {
            System.out.println("Error while removing Server: " + id);
        }
    }

    public void removeUser(User user, Guild guild) {
        try {
            storageObj.getJSONObject(STORAGE.key())
                    .getJSONObject(SERVERS.key())
                    .getJSONObject(guild.getId()).remove(user.getId());
            changes = true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsUser(User user, Guild server, JsonSpecifier object) {
        try {
            switch (object) {
                case STATS -> {
                    return getUser(user, server).getJSONObject(STATS.key()) != null;
                }
                case NAME -> {
                    return getUser(user, server).getString(NAME.key()) != null;
                }
                case STATS_CHATPOINTS_POINTS -> {
                    return getUser(user, server).getJSONObject(STATS.key()).getInt(STATS_CHATPOINTS_POINTS.key()) != 0;
                }
                case STATS_CHATPOINTS_LEVEL -> {
                    return getUser(user, server).getJSONObject(STATS.key()).getInt(STATS_CHATPOINTS_LEVEL.key()) != 0;
                }
                default -> {
                    return false;
                }
            }
        } catch (JSONException e) {
            return false;
        }
    }

    public JSONObject getUsers(Guild guild) {
        try {
            JSONObject usr = getGuild(guild).getJSONObject(USERS.key());
            if (usr == null) {
                changes = true;
                return getGuild(guild).put(USERS.key(), new JSONObject());
            }
            return usr;
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getUser(User user, Guild guild) {
        try {
            JSONObject usr = getUsers(guild).getJSONObject(user.getId());
            getGuild(guild).getJSONObject(USERS.key()).put(user.getId(), usr);
            return new JSONObject().put(user.getId(), usr);
        } catch (JSONException e) {
            try {
                JSONObject usr = new JSONObject().put(STATS.key(), new JSONObject()
                                .put(STATS_CHATPOINTS_POINTS.key(), 0)
                                .put(STATS_CHATPOINTS_LEVEL.key(), 0))
                        .put(NAME.key(), user.getName());
                getGuild(guild).getJSONObject(USERS.key()).put(user.getId(), usr);
                return new JSONObject().put(user.getId(), usr);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void checkContain() {
        try {
            if (!storageObj.has(STORAGE.key())) {
                storageObj.put(STORAGE.key(), new JSONObject());
                changes = true;
            }
            if (!storageObj.getJSONObject(STORAGE.key()).has(SERVERS.key())) {
                storageObj.getJSONObject(STORAGE.key()).put(SERVERS.key(), new JSONObject());
                changes = true;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

