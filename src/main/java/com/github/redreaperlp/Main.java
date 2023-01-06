package com.github.redreaperlp;

import com.github.redreaperlp.events.onUserJoin;
import com.github.redreaperlp.mysql.DataStorage;
import com.github.redreaperlp.util.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Main {

    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    public static Config conf = new Config();
    public static HikariDataSource ds;

    public static void main(String[] args) throws InterruptedException, SQLException {
        Main main = new Main();
        main.start();
    }

    public void start() throws InterruptedException, SQLException {
        System.out.println(GREEN + "*** Starting Bot ***" + RESET);
        System.out.println(GREEN + "*** Version:" + YELLOW + " 1.0.0 " + GREEN + "***" + RESET);
        conf.saveConfig();
        DataStorage data = new DataStorage(conf);

        Connection con = DriverManager.getConnection("jdbc:mysql://" + data.host() + ":" + data.port(), data.user(), data.password());
        PreparedStatement ps = con.prepareStatement("USE " + data.database());
        try {
            ps.execute();
        } catch (SQLException e) {
            System.out.println(RED + "*** The database does not exist! ***" + RESET);
            System.out.println(YELLOW + "*** Creating database... ***" + RESET);
            PreparedStatement stmt = con.prepareStatement("CREATE DATABASE IF NOT EXISTS " + data.database());
            stmt.executeUpdate();
            System.out.println(GREEN + "*** Database created! ***" + RESET);
            con.close();
            stmt.close();
        }
        ps.close();


        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + data.host() + ":" + data.port() + "/" + data.database());
        config.setUsername(conf.getConfig("mysql.user"));
        config.setPassword(conf.getConfig("mysql.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        try {
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            System.out.println(RED + "*** Could not connect to the database! " + YELLOW + " (" + e.getMessage() + ") " + RED + "***" + RESET);
            System.exit(0);
        }
        System.out.println(GREEN + "*** Connected to the database! ***" + RESET);

        prepareTables();

        JDABuilder build = JDABuilder.createDefault(conf.getConfig("token"));
        enableIntents(build);
        build.setStatus(OnlineStatus.ONLINE);
        build.addEventListeners(new onUserJoin());

        JDA jda = build.build();
        jda.awaitReady();
        System.out.println(GREEN + "*** Bot is ready! ***" + RESET);

    }

    public void prepareTables() {
        try {
            Connection con = ds.getConnection();
            createTable("users", con, con.prepareStatement("CREATE TABLE users (id INT NOT NULL AUTO_INCREMENT, discord_id VARCHAR(18) NOT NULL, PRIMARY KEY (id))"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(String table, Connection con, PreparedStatement stmt) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM " + table + " LIMIT 1");
            ps.executeQuery();
            ps.close();
        } catch (SQLException e) {
            System.out.println(YELLOW + "*** Table " + table + " does not exist! ***" + RESET);
            System.out.println(YELLOW + "*** Creating... ***" + RESET);
            stmt.executeUpdate();
            System.out.println(GREEN + "*** Table " + table + " created! ***" + RESET);
        }
    }

    public void enableIntents(JDABuilder build) {
        build.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        build.enableIntents(GatewayIntent.GUILD_MEMBERS);
        build.enableIntents(GatewayIntent.GUILD_PRESENCES);
        build.enableIntents(GatewayIntent.GUILD_MESSAGES);
        build.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        build.enableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS);
        build.enableIntents(GatewayIntent.DIRECT_MESSAGES);
        build.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        build.enableIntents(GatewayIntent.SCHEDULED_EVENTS);
    }
}
