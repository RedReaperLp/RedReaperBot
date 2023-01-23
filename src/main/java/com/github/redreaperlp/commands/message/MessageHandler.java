package com.github.redreaperlp.commands.message;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.stream.Stream;

public class MessageHandler implements Runnable {
    List<String> badThings = Stream.of("https://", "http").toList();
    MessageReceivedEvent e;
    MessageHandleEnum actionEnum;
    Guild guild;
    User user;

    public MessageHandler(MessageReceivedEvent e, MessageHandleEnum actionEnum) {
        this.e = e;
        this.actionEnum = actionEnum;
        this.guild = e.getGuild();
        this.user = e.getAuthor();
    }

    @Override
    public void run() {
        switch (actionEnum) {
            case INCREMENT_CHATPOINTS -> {
                boolean leveledUp = RedReaperBot.servers.chatPoints().increment(guild, user);
                if (leveledUp) {
                    e.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("You leveled up!").queue());
                }
            }
            case CKECK_FOR_LINKS -> {
                if (e.getAuthor().isBot()) return;
                for (String badThing : badThings) {
                    if (e.getMessage().getContentRaw().contains(badThing)) {
                        TextChannel channel = null;
                        PrivateChannel channelPr = null;
                        try {
                            channel = RedReaperBot.jda.getTextChannelById(RedReaperBot.servers.channelConfigurations().getChannelConfigurations(guild, ChannelConfigEn.BAD_WORDS_ASK));
                        } catch (Exception ex) {
                            User user = e.getGuild().getOwner().getUser();
                            channelPr = user.openPrivateChannel().complete();
                        }

                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(":bangbang: | Bad Word")
                                .addField("User", e.getAuthor().getAsMention(), true)
                                .addField("Channel", e.getChannel().getAsMention(), true)
                                .addField("Message", e.getMessage().getContentRaw(), false)
                                .setColor(0xff0000)
                                .setFooter("ID: " + e.getAuthor().getId())
                                .setThumbnail(e.getAuthor().getAvatarUrl());

                        Button keep = Button.success("keepBad", "Keep");
                        Button delete = Button.danger("deleteBad", "Delete");

                        Button ban = Button.danger("ban", "Ban");
                        if (channel != null) {
                            Message msg = channel.sendMessageEmbeds(embed.build()).setActionRow(keep, delete).complete();
                            RedReaperBot.servers.messageAssociation().addAssociation(guild, msg, e.getMessage(), e.getChannel());
                        } else if (channelPr != null) {
                            embed.addField("Server", e.getGuild().getId() + " | " + e.getGuild().getName(), false);
                            Message msg = channelPr.sendMessageEmbeds(embed.build()).setActionRow(keep, delete, ban).complete();
                            RedReaperBot.servers.messageAssociation().addAssociation(guild, msg, e.getMessage(), e.getChannel());
                        }
                        return;
                    }
                }
            }
        }
    }
}
