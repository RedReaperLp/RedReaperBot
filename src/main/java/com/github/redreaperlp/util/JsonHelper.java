package com.github.redreaperlp.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class JsonHelper {
    private static boolean changesUsersettings = false;
    private static boolean changesServers = false;

    public static void serverFinalizer(File file, JSONObject toSave) {
        finalize(changesServers, file, toSave);
        changesServers = false;
    }

    public static void usersettingsFinalizer(File file, JSONObject toSave) {
        finalize(changesUsersettings, file, toSave);
        changesUsersettings = false;
    }

    private static void finalize(boolean changes, File file, JSONObject toSave) {
        try {
            if (changes) {
                BufferedWriter writer = null;
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(toSave.toString(4));
                writer.flush();
                writer.close();
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
     * @param file The file to resolve
     */

    public static JSONObject resolver(File file) {
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
                return new JSONObject();
            } else {
                return new JSONObject(lines);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void usersettingsChange() {
        changesUsersettings = true;
    }

    public static void serversChange() {
        changesServers = true;
    }
}
