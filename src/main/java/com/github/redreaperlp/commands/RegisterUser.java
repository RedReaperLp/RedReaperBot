package com.github.redreaperlp.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RegisterUser extends ListenerAdapter {
    public void onSlashCommandInteraction (SlashCommandInteractionEvent e) {
        if (e.getName().equals("register")) {
            e.reply("Registered " + e.getOption("user").getAsUser().getAsTag()).setEphemeral(true).queue();
        }
    }
}
