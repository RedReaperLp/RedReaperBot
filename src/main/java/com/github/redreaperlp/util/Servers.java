package com.github.redreaperlp.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Servers {
    File file = new File("storage.json");

    JSONObject servers = new JSONObject();
    JSONObject users = new JSONObject();

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
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(servers.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resolves the StorageFile and returns it to the local variable #servers
     * @see #servers
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
            servers = new JSONObject(lines);
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
        Iterator<String> keys = servers.keys();
        while (keys.hasNext()) {
            ids.add(Integer.parseInt(keys.next()));
        }
        System.out.println(ids);
        return ids;
    }

    /**
     * Adds a Server to Config, if it isn´t already
     * @param id ID of the Server in String format
     */
    public void addServer(String id) {
        try {
            if (!servers.has(id)) {
                servers.put(id, new JSONObject());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeServer(int id) {
        servers.remove(String.valueOf(id));
    }

    public void addUser(String id, String server) {
        try {
            servers.getJSONObject(String.valueOf(server)).put(String.valueOf(id), new JSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUser(int id, int server) {
        try {
            servers.getJSONObject(String.valueOf(server)).remove(String.valueOf(id));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsUser(int id, int server) {
        try {
            return servers.getJSONObject(String.valueOf(server)).has(String.valueOf(id));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
