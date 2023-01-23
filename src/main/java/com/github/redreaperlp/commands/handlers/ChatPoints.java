package com.github.redreaperlp.commands.handlers;

import com.github.redreaperlp.RedReaperBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_LEVEL;
import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_POINTS;

public class ChatPoints {

    public ChatPoints(SlashCommandInteractionEvent e) {
        chatpoints(e);
    }

    public void chatpoints(SlashCommandInteractionEvent e) {
        try {
            JSONObject stats = RedReaperBot.servers.stats().getStats(e.getUser(), e.getGuild());

            int points = stats.getInt(STATS_CHATPOINTS_POINTS.key());
            int level = stats.getInt(STATS_CHATPOINTS_LEVEL.key());
            int[] pointStats = RedReaperBot.chatPoints.calcExp(points);
            int remaining = pointStats[1];
            if (points < 23) {
                remaining = 23 - points;
            }
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
