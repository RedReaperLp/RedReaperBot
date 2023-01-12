package com.github.redreaperlp.commands;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.enums.UserObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventHandler extends ListenerAdapter {
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            System.out.println(e.getName());
            Main.servers.addUser(e.getGuild(), e.getUser());
            if (e.getName().equals(CommandEn.BAN.key())) {
                e.getChannel().sendMessage("This command is not yet implemented!").queue();
            } else if (e.getName().equals(CommandEn.CHATPOINTS.key())) {
                chatpoints(e);
            } else if (e.getName().equals(CommandEn.CLEAR.key())) {
                clear(e);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        super.onButtonInteraction(e);
        if (e.isFromGuild()) {
            Main.servers.addUser(e.getGuild(), e.getUser());
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        if (e.isFromGuild()) {
            Main.servers.addUser(e.getGuild(), e.getUser());
            e.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild()) {
            Guild guild = e.getGuild();
            User user = e.getAuthor();

            Main.servers.addUser(guild, user);
            Main.servers.setUser(guild, user, UserObject.STATS_CHATPOINT);
        } else {

        }
    }

    public void chatpoints(SlashCommandInteractionEvent e) {
        JSONObject user = Main.servers.getUser(e.getUser(), e.getGuild());
        try {
            int points = user.getJSONObject(UserObject.STATS.key()).getInt(UserObject.STATS_CHATPOINT.key());
            int levels = user.getJSONObject(UserObject.STATS.key()).getInt(UserObject.STATS_LEVEL.key());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Overview");
            eb.addField("Chatpoints", String.valueOf(points), true);
            eb.addField("Level", String.valueOf(levels), true);
            eb.addField("Remaining Chatpoints", "Needed to level up: " + (Main.servers.calcExp(points)[0] + 1) + " points", false);
            eb.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
            eb.setFooter("Requested by " + e.getUser().getName(), e.getUser().getAvatarUrl());
            eb.setColor(0x00ff00);
            e.replyEmbeds(eb.build()).queue();
        } catch (JSONException ex) {
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
    }

    public void clear(SlashCommandInteractionEvent e) {
        boolean clearUserMSG = false;

        OptionMapping userOption = e.getOption("user");
        if (userOption != null) {
            clearUserMSG = true;
        }

        int toClear = e.getOption("amount").getAsInt();
        MessageHistory his = e.getChannel().getHistoryBefore(e.getChannel().getLatestMessageId(), 100).complete();
        List<Message> messages = his.getRetrievedHistory();
        int cleared = 0;

        if (clearUserMSG) {
            for (Message msg : messages) {
                if (msg.getAuthor().equals(userOption.getAsUser())) {
                    msg.delete().queue();
                    cleared++;
                }
                if (cleared == toClear) {
                    break;
                }
            }
        } else {
            for (Message msg : messages) {
                if (!msg.isEphemeral()) {
                    cleared++;
                }
                msg.delete().queue();
                if (cleared == toClear) {
                    break;
                }
            }
        }
    }
}