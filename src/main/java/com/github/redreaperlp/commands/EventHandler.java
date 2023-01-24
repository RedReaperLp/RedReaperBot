package com.github.redreaperlp.commands;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.commands.handlers.ChatPoints;
import com.github.redreaperlp.commands.message.MessageHandleEnum;
import com.github.redreaperlp.commands.message.MessageHandler;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.enums.IDEnum;
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
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
                new ChatPoints(e);
            } else if (e.getName().equals(CommandEn.CLEAR.key())) {
                e.deferReply().queue();
                new Thread(new DeleterThread(e)).run();
            } else if (e.getName().equals(CommandEn.BAD_WORDS_CHANNEL.key())) {
                OptionMapping mapping = e.getOption(IDEnum.CHANNEL.key());
                String channel = null;
                if (mapping != null) {
                    channel = mapping.getAsChannel().getId();
                } else {
                    channel = e.getChannel().getId();
                }
                RedReaperBot.channelConfigurations.setChannelConfigurations(e.getGuild(), ChannelConfigEn.BAD_WORDS_ASK, channel);
                RedReaperBot.servers.changes();
                e.replyEmbeds(
                        new EmbedBuilder()
                                .setTitle("Bad Words Channel")
                                .setDescription("The bad words channel has been set to " + RedReaperBot.jda.getTextChannelById(channel).getAsMention())
                                .setColor(0xff00).build()
                ).queue();
            } else if (e.getName().equals(CommandEn.ADD_BAD_WORD.key())) {
                RedReaperBot.badMessages.addBadMessage(e);
            } else if (e.getName().equals(CommandEn.REMOVE_BAD_WORD.key())) {
                RedReaperBot.badMessages.removeBadMessage(e);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        String id = e.getComponentId();
        Message space = e.getMessage();
        IDEnum idEnum = IDEnum.fromKey(id);
        switch (idEnum) {
            case DELETE_BADWORD -> {
                Guild guild = guildByEmbed(e, space);
                String[] association = RedReaperBot.messageAssociation.getAssociation(guild, e.getChannel(), space);
                if (association != null) {
                    RedReaperBot.servers.changes();
                    TextChannel tChannel = RedReaperBot.jda.getTextChannelById(association[1]);
                    VoiceChannel vChannel = RedReaperBot.jda.getVoiceChannelById(association[1]);
                    if (tChannel != null) {
                        try {
                            tChannel.retrieveMessageById(association[0]).complete().delete().queue();
                        } catch (Exception exception) {
                            e.reply("The message was already deleted!").setEphemeral(true).queue();
                            return;
                        }
                    } else if (vChannel != null) {
                        try {
                            tChannel.retrieveMessageById(association[0]).complete().delete().queue();
                        } catch (Exception exception) {
                            e.reply("The message was already deleted!").setEphemeral(true).queue();
                            return;
                        }
                    }
                    RedReaperBot.messageAssociation.removeAssociation(guild, e.getChannel(), space);
                } else {
                    e.reply("There was no association found for this message!").setEphemeral(true).queue();
                    return;
                }
                e.deferEdit().queue();
                space.delete().queue();
            }
            case KEEP_BADWORD -> {
                e.deferReply().queue();
                Guild guild = guildByEmbed(e, space);
                space.delete().queue();
                boolean foundAssociation = RedReaperBot.messageAssociation.removeAssociation(guild, e.getChannel(), space);
                if (!foundAssociation) {
                    e.getHook().sendMessage("There was no association found for this message!").queue();
                } else {
                    e.getHook().sendMessage("The association has been removed!").queue();
                    RedReaperBot.servers.changes();
                }
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
            if (e.getMessage().getContentRaw().equals("clear")) {
                List<Message> toDelete = new ArrayList<>();
                List<Message> messages = e.getChannel().asPrivateChannel().getHistory().retrievePast(100).complete();
                for (Message message : messages) {
                    if (message.getAuthor().equals(RedReaperBot.jda.getSelfUser()) && !message.getContentRaw().equals("There are no messages to delete!")) {
                        toDelete.add(message);
                    }
                }

                if (toDelete.size() == 0) {
                    e.getMessage().getAuthor().openPrivateChannel().complete().sendMessage("There are no messages to delete!").queue(message -> {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                message.delete().queue();
                            }
                        }, 5000);
                    });
                    return;
                }
                e.getChannel().asPrivateChannel().purgeMessages(toDelete);
            }
        }
    }
}

