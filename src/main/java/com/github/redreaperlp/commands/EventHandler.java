package com.github.redreaperlp.commands;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.util.thread.DeleterThread;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class EventHandler extends ListenerAdapter {
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            if (e.getName().equals(CommandEn.BAN.key())) {
                e.getChannel().sendMessage("This command is not yet implemented!").queue();
            } else if (e.getName().equals(CommandEn.CHATPOINTS.key())) {
                chatpoints(e);
            } else if (e.getName().equals(CommandEn.CLEAR.key())) {
                e.deferReply().queue();
                new DeleterThread(e).run();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        super.onButtonInteraction(e);
        if (e.isFromGuild()) {
            Main.servers.getUser(e.getUser(), e.getGuild());
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        if (e.isFromGuild()) {
            Main.servers.getUser(e.getUser(), e.getGuild());
            e.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild()) {
            Guild guild = e.getGuild();
            User user = e.getAuthor();
            Main.chatPoints.increment(guild, user);
        } else {

        }
    }

    public void chatpoints(SlashCommandInteractionEvent e) {
        try {
            JSONObject stats = Main.servers.getUser(e.getUser(), e.getGuild()).getJSONObject(e.getUser().getId()).getJSONObject(STATS.key());

            int points = stats.getInt(STATS_CHATPOINTS_POINTS.key());
            int level = stats.getInt(STATS_CHATPOINTS_LEVEL.key());
            int[] pointStats = Main.chatPoints.calcExp(points);
            int remaining = pointStats[1];
            if (points != 0) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Overview");
                eb.addField("Chatpoints", points + "", true);
                eb.addField("Level", level + "", true);
                eb.addField("Remaining Chatpoints", "Needed to level up: " + remaining + " points", false);
                eb.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
                eb.setFooter("Requested by " + e.getUser().getName(), e.getUser().getAvatarUrl());
                eb.setColor(0x00ff00);
                e.replyEmbeds(eb.build()).queue();
            } else {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Overview");
                eb.addField("Chatpoints", "0", true);
                eb.addField("Level", "1", true);
                eb.addField("Remaining Chatpoints", "Needed to level up: 23 points", false);
                eb.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
                eb.setFooter("Requested by " + e.getUser().getName(), e.getUser().getAvatarUrl());
                eb.setColor(0xff0000);
                e.replyEmbeds(eb.build()).queue();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }
}