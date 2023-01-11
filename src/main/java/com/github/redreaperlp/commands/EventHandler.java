package com.github.redreaperlp.commands;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.UserObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONException;
import org.json.JSONObject;

public class EventHandler extends ListenerAdapter {
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";
    String RESET = "\u001B[0m";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        System.out.println(e.getName());
        Main.servers.addUser(e.getGuild(), e.getUser());
        if (e.getName().equals("register")) {
            e.getChannel().sendMessage("This command is not yet implemented!").addActionRow(
                    Button.danger("register", "Register")
            ).queue();
        } else if (e.getName().equals("ban")) {
            e.getChannel().sendMessage("This command is not yet implemented!").queue();
        } else if (e.getName().equals("chatpoints")) {
            chatpoints(e);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        super.onButtonInteraction(e);
        Main.servers.addUser(e.getGuild(), e.getUser());
        if (e.getButton().getId().equals("register")) {
            e.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        Main.servers.addUser(e.getGuild(), e.getUser());
        e.reply("This command is not yet implemented!").setEphemeral(true).queue();
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        User user = e.getAuthor();

        Main.servers.addUser(guild, user);
        Main.servers.setUser(guild, user, UserObject.STATS_CHATPOINT);
        Main.servers.finalizer();
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
            eb.addField("Remaining Chatpoints","Needed to level up: " + String.valueOf(Main.servers.calcRemaining(points) + " points"), false);
            eb.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
            eb.setFooter("Requested by " + e.getUser().getName(), e.getUser().getAvatarUrl());
            eb.setColor(0x00ff00);
            e.replyEmbeds(eb.build()).queue();
        } catch (JSONException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Overview");
            eb.addField("Chatpoints", "0", true);
            eb.addField("Level", "1", true);
            eb.addField("Remaining Chatpoints","Needed to level up: 10 points", false);
            eb.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
            eb.setFooter("Requested by " + e.getUser().getName(), e.getUser().getAvatarUrl());
            eb.setColor(0xff0000);
            e.replyEmbeds(eb.build()).queue();
        }
    }
}
