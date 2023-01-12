package com.github.redreaperlp.util;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.UserObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.redreaperlp.enums.UserObject.*;

public class Servers {
    File file = new File(Main.conf.getConfig("storage"));

    JSONObject serversObj = new JSONObject();
    boolean changes = false;

    public Servers() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void finalizer() {
        try {
            if (changes) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(serversObj.toString(4));
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
     * @see #serversObj
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
                serversObj = new JSONObject();
            } else {
                serversObj = new JSONObject(lines);
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
        Iterator<String> keys = serversObj.keys();
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
            if (!serversObj.has(guild.getId())) {
                serversObj.put(guild.getId(), new JSONObject());
                changes = true;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getServer(Guild guild) {
        try {
            return serversObj.getJSONObject(guild.getId());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public void removeServer(int id) {
        serversObj.remove(String.valueOf(id));
        changes = true;
    }

    public void addUser(Guild server, User user) {
        try {
            if (!serversObj.has(server.getId())) {
                addServer(server);
            }

            JSONObject serverObject = serversObj.getJSONObject(server.getId());
            if (!serverObject.has(user.getId())) {
                serverObject.put(user.getId(), new JSONObject()
                                .put(UserObject.NAME.key(), user.getName())
                                .put(STATS.key(), new JSONObject()
                                        .put(STATS_CHATPOINT.key(), 0)
                                        .put(STATS_LEVEL.key(), 0)));
                changes = true;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUser(User user, Guild guild) {
        try {
            serversObj.getJSONObject(guild.getId()).remove(user.getId());
            changes = true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsUser(User user, Guild server, UserObject object) {
        try {
            switch (object) {
                case STATS:
                    return serversObj.getJSONObject(server.getId()).getJSONObject(user.getId()).getJSONObject("stats") != null;
                case NAME:
                    return serversObj.getJSONObject(server.getId()).getJSONObject(user.getId()).getString("name") != null;
            }
        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    public JSONObject getUser(User user, Guild server) {
        try {
            return serversObj.getJSONObject(server.getId()).getJSONObject(user.getId());
        } catch (JSONException e) {
            return null;
        }
    }

    public void setUser(Guild server, User user, UserObject key) {
        try {
            JSONObject serverObj = serversObj.getJSONObject(server.getId());
            switch (key) {
                case STATS -> {
                    serverObj.put("stats", new JSONObject().put("chatpoints", 0));
                }
                case NAME -> {
                    serverObj.getJSONObject(user.getId()).put("name", user.getName());
                }
                case STATS_CHATPOINT -> {
                    int chatpoints = serverObj.getJSONObject(user.getId()).getJSONObject(STATS.key()).getInt(STATS_CHATPOINT.key());
                    serverObj.getJSONObject(user.getId()).getJSONObject(STATS.key())
                            .put(STATS_CHATPOINT.key(), chatpoints + 1)
                            .put(STATS_LEVEL.key(), calcLevel(chatpoints + 1));

                }
            }
            changes = true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int calcLevel(int chatpoints) {
        int level = 0;
        int pointCost = 10;
        while (chatpoints > pointCost) {
            level++;
            pointCost += level * 1.3;
            chatpoints -= pointCost;
        }
        return level == 0 ? 1 : level;
    }

    public int calcRemaining(int chatpoints) {
        int level = 0;
        int pointCost = 10;
        while (chatpoints > pointCost) {
            level++;
            pointCost += level * 1.3;
            chatpoints -= pointCost;
        }
        return pointCost - chatpoints;
    }
}

