package com.github.redreaperlp.util;

import com.github.redreaperlp.enums.UserObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.redreaperlp.enums.UserObject.STATS;

public class Servers {
    File file = new File("storage.json");

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

    public void removeServer(int id) {
        serversObj.remove(String.valueOf(id));
        changes = true;
    }

    public void addUser(User user, Guild server) {
        try {
            if (!serversObj.has(server.getId())) {
                JSONObject serverObj = new JSONObject();
                serverObj.put("users", new JSONObject());
                serversObj.put(server.getId(), serverObj);
                changes = true;
            }

            JSONObject obj = serversObj.getJSONObject(server.getId());
            if (!obj.has(user.getId())) {
                obj.put(user.getId(), new JSONObject().put("name", user.getName()));
                changes = true;
            }
            JSONObject userObj = obj.getJSONObject(user.getId());
            if (!userObj.has("name")) {
                userObj.put("name", user.getName());
                changes = true;
            } else if (!userObj.has(STATS.key())) {
                obj.put(user.getId(), userObj.put(STATS.key(), new JSONObject())).put(UserObject.STATS_CHATPOINT.key(), 0);
                changes = true;
            }
            JSONObject statsObj = userObj.getJSONObject(STATS.key());
            if (!statsObj.has(UserObject.STATS_CHATPOINT.key())) {
                statsObj.put(UserObject.STATS_CHATPOINT.key(), 0);
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

    public void setUser(Guild server, User user, UserObject key, JSONObject value) {
        try {
            JSONObject serverObj = serversObj.getJSONObject(server.getId());
            JSONObject temp = serverObj.getJSONObject(user.getId());
            JSONObject stats = temp.getJSONObject(STATS.key());
            switch (key) {
                case STATS, NAME -> temp.put(user.getId() ,setHelper(temp, key, value));
                case STATS_CHATPOINT -> stats.put(STATS.key() ,setHelper(temp.getJSONObject("stats"), key, value));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject setHelper(JSONObject object, UserObject key, JSONObject value) {
        try {
            return object.put(key.key(), value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

