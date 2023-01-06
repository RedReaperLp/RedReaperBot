package com.github.redreaperlp.mysql;

import com.github.redreaperlp.util.Config;

import java.net.MalformedURLException;
import java.net.URL;

public class DataStorage {
    String RED = "\u001B[31m";
    String RESET = "\u001B[0m";

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public DataStorage(String host, String port, String database, String user, String password) {
        this.host = host;
        try {
        this.port = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            System.out.println(RED + "*** The port is not a number! ***" + RESET);
            System.exit(0);
        }
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public DataStorage(Config instance) {

        this(instance.getConfig("mysql.host"),
                instance.getConfig("mysql.port"),
                instance.getConfig("mysql.database"),
                instance.getConfig("mysql.user"),
                instance.getConfig("mysql.password"));
}

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public String database() {
        return database;
    }

    public String user() {
        return user;
    }

    public String password() {
        return password;
    }

    public String toString() {
        return "DataStorage{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'';
    }

    public URL toURL() {
        try {
            return new URL("jdbc:mysql://" + host + ":" + port + "/" + database);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
