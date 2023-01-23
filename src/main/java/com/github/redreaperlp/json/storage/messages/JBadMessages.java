package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.IDEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.github.redreaperlp.enums.JsonSpecifier.BAD_MESSAGES;

public class JBadMessages {
    public void addBadMessage(SlashCommandInteractionEvent e) {
        try {
            String word = e.getOption(IDEnum.WORD.key()).getAsString();
            if (getBadMessages(e.getGuild()).contains(word)) {
                e.reply("This word is already a bad message!").setEphemeral(true).queue();
                return;
            }
            JSONObject guild = RedReaperBot.servers.getGuild(e.getGuild());
            if (!guild.has(BAD_MESSAGES.key())) {
                guild.put(BAD_MESSAGES.key(), "");
            }
            String badMessages = guild.getString(BAD_MESSAGES.key());
            badMessages += word + ";";
            guild.put(BAD_MESSAGES.key(), badMessages);
            RedReaperBot.servers.changes();


            e.reply("Bad message added!").setEphemeral(true).queue();
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeBadMessage(SlashCommandInteractionEvent e) {
        try {
            String word = e.getOption(IDEnum.WORD.key()).getAsString();
            if (!getBadMessages(e.getGuild()).contains(word)) {
                e.reply("This word is not a bad message!").setEphemeral(true).queue();
                return;
            }
            JSONObject guild = RedReaperBot.servers.getGuild(e.getGuild());
            String badMessages = guild.getString(BAD_MESSAGES.key());
            badMessages = badMessages.replace(word + ";", "");
            guild.put(BAD_MESSAGES.key(), badMessages);
            RedReaperBot.servers.changes();
            e.reply("Bad message removed!").setEphemeral(true).queue();
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<String> getBadMessages(Guild guild) {
        try {
            JSONObject jGuild = RedReaperBot.servers.getGuild(guild);
            if (!jGuild.has(BAD_MESSAGES.key())) {
                jGuild.put(BAD_MESSAGES.key(), "");
            }
            String badMessages = jGuild.getString(BAD_MESSAGES.key());
            String[] splitted = badMessages.split(";");
            List<String> list = new ArrayList<>();
            for (String s : splitted) {
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            return List.of(badMessages.split(";"));
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
