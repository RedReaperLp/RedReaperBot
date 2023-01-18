package com.github.redreaperlp;

import com.github.redreaperlp.commands.EventHandler;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.enums.ConfEnum;
import com.github.redreaperlp.events.OnUserJoin;
import com.github.redreaperlp.util.CommandOptions;
import com.github.redreaperlp.util.Config;
import com.github.redreaperlp.util.storage.servers.Servers;
import com.github.redreaperlp.util.storage.servers.stats.util.ChatPoints;
import com.github.redreaperlp.util.thread.FinalizerThread;
import com.github.redreaperlp.util.thread.SaveCaller;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;
import java.util.Locale;

public class Main {

    public static Servers servers;
    public static ChatPoints chatPoints;
    String GREEN = "\u001B[32m";
    String RED = "\u001B[31m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    public static Config conf;


    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        System.out.println(main.YELLOW + "*** Starting Bot ***" + main.RESET);
        System.out.println(main.YELLOW + "*** Version:" + main.GREEN + " 1.0.0 " + main.YELLOW + "***" + main.RESET);
        conf = new Config();
        servers = new Servers();
        chatPoints = new ChatPoints();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(new SaveCaller()));
        main.start();
    }

    public void start() throws InterruptedException {
        JDABuilder build = JDABuilder.createDefault(conf.getConfig(ConfEnum.TOKEN.key()));
        build.setActivity(Activity.playing(conf.getConfig(ConfEnum.PLAYING.key())));
        build.setStatus(OnlineStatus.ONLINE);
        build.setEventPassthrough(true);
        build.addEventListeners(new OnUserJoin());
        build.addEventListeners(new EventHandler());
        enableIntents(build);
        JDA jda = null;
        int tryCount = 0;
        System.out.println(YELLOW + "*** Connecting to Discord ***" + RESET);
        while (jda == null) {
            try {
                jda = build.build();
                System.out.println(GREEN + "*** Connected to Discord ***" + RESET);
            } catch (Exception e) {
                if (e.getMessage().contains("UnknownHostException")) {
                    if (tryCount == 5) {
                        System.out.println(YELLOW + "Could not connect to Discord. Please check your internet connection." + RESET);
                        System.exit(0);
                    }
                    System.out.println(RED + "Make sure, you have a wifi Connection, retry in 5 Seconds" + RESET);
                    tryCount++;
                    Thread.sleep(5000);
                } else if (e.getMessage().toLowerCase(Locale.ROOT).contains("token may")) {
                    System.out.println(RED + "Please check your Token in the config.yaml" + RESET);
                    System.exit(0);
                } else {
                    System.out.println(RED + e.getMessage() + RESET);
                    System.exit(0);
                }
            }
        }
        jda.awaitReady();
        System.out.println(GREEN + "*** Bot is ready! ***" + RESET);

        List<Guild> currentServers = jda.getGuilds();
        for (Guild g : currentServers) {
            servers.addGuild(g);
            g.updateCommands().addCommands(
                    prepareCommand(CommandEn.BAN),
                    Commands.slash("register", "Register yourself to the server"),
                    Commands.user("register"),
                    prepareCommand(CommandEn.CHATPOINTS),
                    prepareCommand(CommandEn.CLEAR)
            ).queue();
        }
        new Thread(new FinalizerThread()).start();
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

    public SlashCommandData prepareCommand(CommandEn c) {
        SlashCommandData command = Commands.slash(c.key(), c.description());
        CommandOptions[] ops = c.options();
        if (ops != null) {
            for (CommandOptions options : ops) {
                command.addOption(options.type(), options.name(), options.description(), options.forced());
            }
        }
        return command;
    }

}
