package com.github.redreaperlp.util;

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
    File file = new File(Main.conf.getConfig("storage"));

    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";
    JSONObject storageObj = new JSONObject();
    JSONObject serverObj;
    boolean changes = false;

    public Servers() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                serverObj = storageObj.put(SERVERS.key(), new JSONObject());
                changes = true;
                finalizer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            resolver();
            System.out.println("HI");
            try {
                serverObj = storageObj.getJSONObject(SERVERS.key());
                System.out.println(serverObj);
                return;
            } catch (JSONException e) {
                System.out.println(YELLOW + "Error while getting server object, creating...\n" + e.getMessage() + RESET);
            }
            if (serverObj == null) {
                try {
                    serverObj = storageObj.put(SERVERS.key(), new JSONObject());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void finalizer() {
        try {
            if (changes) {
                storageObj.put(SERVERS.key(), serverObj);
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
        System.out.println(ids);
        return ids;
    }

    /**
     * Adds a Server to Config, if it isn´t already
     *
     * @param guild The Guild to add
     */
    public void addServer(Guild guild) {
        try {
            if (!serverObj.getJSONObject(SERVERS.key()).has(guild.getId())) {
                serverObj.getJSONObject(SERVERS.key()).put(guild.getId(), new JSONObject().put(USERS.key(), new JSONObject()));
                changes = true;
            }
            System.out.println(serverObj.toString(4));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getServer(Guild guild) {
        try {
            return serverObj.getJSONObject(SERVERS.key()).getJSONObject(guild.getId());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public void removeServer(String id) {
        try {
            serverObj.getJSONObject(SERVERS.key()).remove(id);
            changes = true;
        } catch (JSONException e) {
            System.out.println("Error while removing Server: " + id);
        }
    }

    public void addUser(Guild server, User user) {
        try {
            if (!serverObj.has(server.getId())) {
                addServer(server);
            }

            JSONObject userObj = getUsers(server);
            if (userObj == null) {
                 getUsers(server).put(user.getId(), new JSONObject()
                        .put(JsonSpecifier.NAME.key(), user.getName())
                        .put(STATS.key(), new JSONObject()
                                .put(STATS_CHATPOINT_POINTS.key(), 0)
                                .put(STATS_CHATPOINTS_LEVEL.key(), 0)));
                changes = true;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUser(User user, Guild guild) {
        try {
            serverObj.getJSONObject(guild.getId()).remove(user.getId());
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
                case STATS_CHATPOINT_POINTS -> {
                    return getUser(user, server).getJSONObject(STATS.key()).getInt(STATS_CHATPOINT_POINTS.key()) != 0;
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

    public JSONObject getUsers(Guild server) {
        try {
            return getServer(server).getJSONObject(USERS.key());
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getUser(User user, Guild server) {
        try {
            return getUsers(server).getJSONObject(user.getId());
        } catch (JSONException e) {
            return null;
        }
    }
}

