package com.github.redreaperlp.commands;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.commands.message.MessageHandleEnum;
import com.github.redreaperlp.commands.message.MessageHandler;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import com.github.redreaperlp.util.thread.DeleterThread;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.List;

import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_LEVEL;
import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_POINTS;

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
                new Thread(new DeleterThread(e)).run();
            } else if (e.getName().equals(CommandEn.BAD_WORDS_CHANNEL.key())) {
                OptionMapping mapping = e.getOption("channel");
                String channel = null;
                if (mapping != null) {
                    channel = mapping.getAsChannel().getId();
                } else {
                    channel = e.getChannel().getId();
                }
                RedReaperBot.servers.channelConfigurations().setChannelConfigurations(e.getGuild(), ChannelConfigEn.BAD_WORDS_ASK, channel);
                RedReaperBot.servers.changes();
                e.replyEmbeds(
                        new EmbedBuilder()
                                .setTitle("Bad Words Channel")
                                .setDescription("The bad words channel has been set to " + RedReaperBot.jda.getTextChannelById(channel).getAsMention())
                                .setColor(0xff00).build()
                ).queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        String id = e.getComponentId();
        Message space = e.getMessage();
        switch (id) {
            case "deleteBad" -> {
                Guild guild = guildByEmbed(e, space);
                String[] association = RedReaperBot.servers.messageAssociation().getAssociation(guild, space);
                if (association != null) {
                    RedReaperBot.servers.changes();
                    TextChannel tChannel = RedReaperBot.jda.getTextChannelById(association[1]);
                    VoiceChannel vChannel = RedReaperBot.jda.getVoiceChannelById(association[1]);
                    if (tChannel != null) {
                        tChannel.retrieveMessageById(association[0]).queue(message -> message.delete().queue());
                    } else if (vChannel != null) {
                        vChannel.retrieveMessageById(association[0]).queue(message -> message.delete().queue());
                    }
                } else {
                    e.getHook().sendMessage("There was no association found for this message!").setEphemeral(true).queue();
                }
                e.deferEdit().queue();
                space.delete().queue();
            }
            case "keepBad" -> {
                Guild guild = guildByEmbed(e, space);
                boolean foundAssociation = RedReaperBot.servers.messageAssociation().removeAssociation(guild, space);
                if (!foundAssociation) {
                    e.getHook().sendMessage("There was no association found for this message!").queue();
                    System.out.println(RED + "There was no association found for this message!" + RESET);
                } else {
                    e.getHook().sendMessage("The association has been removed!").queue();
                    System.out.println(GREEN + "The association has been removed!" + RESET);
                    RedReaperBot.servers.changes();
                }
                space.delete().queue();
            }
        }
    }


    public Guild guildByEmbed(ButtonInteractionEvent e, Message space) {
        String serverID = null;
        if (space.getChannel() instanceof PrivateChannel) {
            List<MessageEmbed> ebList = space.getEmbeds();
            for (MessageEmbed eb : ebList) {
                for (MessageEmbed.Field field : eb.getFields()) {
                    if (field.getName().equals("Server")) {
                        serverID = field.getValue().split(" ")[0].trim();
                    }
                }
            }
        } else {
            serverID = e.getGuild().getId();
        }
        return RedReaperBot.jda.getGuildById(serverID);
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        if (e.isFromGuild()) {
            RedReaperBot.servers.user().getUser(e.getUser(), e.getGuild());
            e.reply("This command is not yet implemented!").setEphemeral(true).queue();
        }
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild()) {
            Guild guild = e.getGuild();
            User user = e.getAuthor();

            new Thread(new MessageHandler(e, MessageHandleEnum.CKECK_FOR_LINKS)).run();
            new Thread(new MessageHandler(e, MessageHandleEnum.INCREMENT_CHATPOINTS)).run();
        } else {

        }
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