package com.github.redreaperlp.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RegisterUser extends ListenerAdapter {
    public void onSlashCommandInteraction (SlashCommandInteractionEvent e) {
        e.deferReply().setEphemeral(true).queue();
        if (e.getName().equals("register")) {
            e.getHook().sendMessage("This command is not yet implemented!").addActionRow(
                    Button.danger("register", "Register")
            ).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        super.onButtonInteraction(event);
        System.out.println(event.getButton().getId());
        if (event.getButton().getId().equals("register")) {
            event.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }
}
