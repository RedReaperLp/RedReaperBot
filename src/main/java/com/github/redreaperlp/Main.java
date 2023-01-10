package com.github.redreaperlp;

import com.github.redreaperlp.commands.BanCommand;
import com.github.redreaperlp.events.OnUserJoin;
import com.github.redreaperlp.util.Config;
import com.github.redreaperlp.util.Servers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Main {

    static Servers servers = new Servers();
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
        enableIntents(build);
        build.setActivity(Activity.playing(conf.getConfig("playing")));
        build.setStatus(OnlineStatus.ONLINE);
        build.addEventListeners(new OnUserJoin());
        build.addEventListeners(new BanCommand());

        JDA jda = build.build();
        jda.awaitReady();
        System.out.println(GREEN + "*** Bot is ready! ***" + RESET);

        List<Guild> currentServers = jda.getGuilds();

        for (Guild g : currentServers) {
            servers.addServer(g.getId());
            System.out.println(g.getName());
            g.loadMembers().onSuccess(members -> {
                for (Member member : members) {
                    User user = member.getUser();
                    if (user.isBot()) {
                        continue;
                    }
                    System.out.println(user.getName());
                    servers.addUser(user.getId(), g.getId());
                }
            });

            g.updateCommands().addCommands(Commands.slash("register", "Register yourself to the server")).queue();
            g.updateCommands().addCommands(
                    Commands.slash("ban", "Bans a user").
                            addOption(OptionType.USER, "user", "The user to ban", true).
                            addOption(OptionType.STRING, "reason", "The reason for the ban", true).
                            addOption(OptionType.INTEGER, "days", "The amount of days to delete messages", false)
            ).queue();
        }
        servers.finalizer();
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
