package com.github.redreaperlp.commands;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.commands.handlers.ChatPoints;
import com.github.redreaperlp.commands.message.MessageHandleEnum;
import com.github.redreaperlp.commands.message.MessageHandler;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.enums.IDEnum;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import com.github.redreaperlp.json.storage.messages.OAssociation;
import com.github.redreaperlp.util.thread.DeleterThread;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
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
        Guild guild = guildByEmbed(e);
        OAssociation association = RedReaperBot.messageAssociation.getAssociation(guild, space);
        switch (idEnum) {
            case DELETE_BADWORD -> {
                if (association != null) {
                    association.targetMSG().delete().queue();
                    e.reply("The Message has been Deleted!").setEphemeral(true).queue();
                    RedReaperBot.servers.changes();
                    RedReaperBot.messageAssociation.removeAssociation(association, guild);
                } else {
                    e.reply("There was no association found for this message!").setEphemeral(true).queue();
                    e.getMessage().delete().queue();
                    return;
                }
                space.delete().queue();
            }
            case KEEP_BADWORD -> {
                e.deferReply().queue();
                space.delete().queue();
                boolean foundAssociation = RedReaperBot.messageAssociation.removeAssociation(association, guild);
                if (!foundAssociation) {
                    e.getHook().sendMessage("There was no association found for this message!").queue();
                } else {
                    e.getHook().sendMessage("The association has been removed!").queue();
                    RedReaperBot.servers.changes();
                }
            }
        }
    }


    /**
     * Gets the guild from the embed
     * @note The message must have an embed with a field named #IDEnum.CHANNEL.key() with the channel id
     * @param e
     * @return the guild of the embed or null if not found
     */
    public Guild guildByEmbed(ButtonInteractionEvent e) {
        GuildMessageChannel channel;
        for (MessageEmbed.Field field : e.getMessage().getEmbeds().get(0).getFields()) {
            if (field.getName().equals(IDEnum.CHANNEL.key())) {
                channel = (GuildMessageChannel) RedReaperBot.jda.getTextChannelById(field.getValue().replace("<#", "").replace(">", ""));
                return channel.getGuild();
            }
        }
        return null;
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

