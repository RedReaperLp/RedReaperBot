package com.github.redreaperlp.util;

import com.github.redreaperlp.enums.ConfEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Config {

    private List<String> config = new ArrayList<String>();
    private File file = new File("config.yaml");

    public Config() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                genDefault();
                saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner scanner = new Scanner(file);
            config.clear();
            while (scanner.hasNextLine()) {
                config.add(scanner.nextLine());
            }
            scanner.close();
            checkContaining();
            saveConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setConfig(String pos, String value) {
        if (this.contains(pos)) {
            for (int i = 0; i < config.size(); i++) {
                if (config.get(i).startsWith(pos)) {
                    if (value == null) {
                        config.remove(i);
                    } else {
                        config.set(i, pos + "[" + value + "]");
                    }
                }
            }
        } else {
            config.add(pos + "[" + value + "]");
        }
    }

    public void saveConfig() {
        try {
            file.delete();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String s : config) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getConfig(String pos) {
        for (String s : config) {
            if (s.startsWith(pos + "[")) {
                return s.substring(pos.length() + 1, s.length() - 1);
            }
        }
        return null;
    }

    public void genDefault() {
        config.add("// This is the config file for the bot.\n" +
                "// You can change the settings here.\n" +
                "// If you want to change the settings, you have to restart the bot.\n" +
                "\n" +
                "// token: The token of the bot can be found on the discord developer page(https://discord.com/developers/applications).\n" +
                "// playing: The status of the bot.\n" +
                "// storage: The storage type of the bot. (json)\n" +
                "// user_settings: The user settings type of the bot. (json)\n"
        );
    }

    public void checkContaining() {
        this.contains(ConfEnum.TOKEN);
        this.contains(ConfEnum.PLAYING);
        this.contains(ConfEnum.STORAGE);
        this.contains(ConfEnum.USER_SETTINGS);
    }

    public void contains(ConfEnum e) {
        if (!this.contains(e.key())) {
            this.setConfig(e.key(), e.defaultValue());
        }
    }

    public void contains(String pos, String value) {
        if (!this.contains(pos)) {
            this.setConfig(pos, value);
        }
    }

    public boolean contains(String pos) {
        for (String s : config) {
            if (s.startsWith(pos + "[")) {
                return true;
            }
        }
        return false;
    }
}

