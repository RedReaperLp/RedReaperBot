package com.github.redreaperlp;

import com.github.redreaperlp.commands.EventHandler;
import com.github.redreaperlp.events.OnUserJoin;
import com.github.redreaperlp.util.Config;
import com.github.redreaperlp.util.Servers;
import com.github.redreaperlp.util.thread.FinalizerThread;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static Servers servers = new Servers();
    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    public static Config conf = new Config();

    public static void main(String[] args) throws InterruptedException, SQLException {
        Main main = new Main();
        main.start();
    }

    public void start() throws InterruptedException, SQLException {
        System.out.println(GREEN + "*** Starting Bot ***" + RESET);
        System.out.println(GREEN + "*** Version:" + YELLOW + " 1.0.0 " + GREEN + "***" + RESET);
        conf.saveConfig();


        JDABuilder build = JDABuilder.createDefault(conf.getConfig("token"));
        build.setActivity(Activity.playing(conf.getConfig("playing")));
        build.setStatus(OnlineStatus.ONLINE);
        build.setEventPassthrough(true);
        build.addEventListeners(new OnUserJoin());
        build.addEventListeners(new EventHandler());
        enableIntents(build);

        JDA jda = build.build();
        jda.awaitReady();
        System.out.println(GREEN + "*** Bot is ready! ***" + RESET);

        List<Guild> currentServers = jda.getGuilds();
        servers.resolver();
        for (Guild g : currentServers) {
            servers.addServer(g);
            g.updateCommands().addCommands(
                    Commands.slash("ban", "Bans a user").
                            addOption(OptionType.USER, "user", "The user to ban", true).
                            addOption(OptionType.STRING, "reason", "The reason for the ban", true).
                            addOption(OptionType.INTEGER, "days", "The amount of days to delete messages", false),
                    Commands.slash("register", "Register yourself to the server"),
                    Commands.user("register"),
                    Commands.slash("chatpoints", "Shows your chatpoints")

            ).queue();
        }
        new Thread(new FinalizerThread(servers)).start();
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
