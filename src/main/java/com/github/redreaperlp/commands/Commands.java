package com.github.redreaperlp.commands;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.enums.UserObject;
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

public class Commands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        Main.servers.addUser(e.getUser(), e.getGuild());
        e.deferReply().setEphemeral(true).queue();
        if (e.getName().equals("register")) {
            e.getHook().sendMessage("This command is not yet implemented!").addActionRow(
                    Button.danger("register", "Register")
            ).queue();
        } else if (e.getName().equals("ban")) {
            e.getHook().sendMessage("This command is not yet implemented!").queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        super.onButtonInteraction(e);
        Main.servers.addUser(e.getUser(), e.getGuild());
        System.out.println(e.getButton().getId());
        if (e.getButton().getId().equals("register")) {
            e.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        Main.servers.addUser(e.getUser(), e.getGuild());
        e.reply("This command is not yet implemented!").setEphemeral(true).queue();
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            User user = e.getAuthor();
            Guild guild = e.getGuild();
            Main.servers.addUser(user, guild);
            JSONObject userObj = Main.servers.getUser(user, guild);
            JSONObject stats = userObj.getJSONObject(UserObject.STATS.key());
            int chatpoints = stats.getInt(UserObject.STATS_CHATPOINT.key());
            stats.put(UserObject.STATS_CHATPOINT.key(), chatpoints + 1);
            System.out.println(stats.toString(4));
            Main.servers.setUser(guild, user, UserObject.STATS, stats);
            Main.servers.finalizer();
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
