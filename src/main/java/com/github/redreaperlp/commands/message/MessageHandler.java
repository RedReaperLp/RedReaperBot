package com.github.redreaperlp.commands.message;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.IDEnum;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MessageHandler implements Runnable {
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
        if (e.getAuthor().isBot()) return;
        switch (actionEnum) {
            case INCREMENT_CHATPOINTS -> {
                boolean leveledUp = RedReaperBot.chatPoints.increment(guild, user);
                if (leveledUp) {
                    e.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("You leveled up!").queue());
                }
            }
            case CKECK_FOR_LINKS -> {
                for (String badThing : RedReaperBot.badMessages.getBadMessages(guild)) {
                    if (badThing.equals("")) return;
                    if (e.getMessage().getContentRaw().contains(badThing)) {
                        TextChannel channel = null;
                        PrivateChannel channelPr = null;
                        try {
                            channel = RedReaperBot.jda.getTextChannelById(RedReaperBot.channelConfigurations.getChannelConfigurations(guild, ChannelConfigEn.BAD_WORDS_ASK));
                        } catch (Exception ex) {
                            User user = e.getGuild().getOwner().getUser();
                            channelPr = user.openPrivateChannel().complete();
                        }

                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(":bangbang: | Bad Word")
                                .addField(IDEnum.USER.key(), e.getAuthor().getAsMention(), true)
                                .addField(IDEnum.CHANNEL.key(), e.getChannel().getAsMention(), true)
                                .addField(IDEnum.MESSAGE.key(), e.getMessage().getContentRaw(), false)
                                .setColor(0xff0000)
                                .setImage("https://cdn.discordapp.com/attachments/1060601917878829226/1067565820428943500/ReportBanner.png")
                                .setThumbnail(e.getGuild().getIconUrl());

                        Button keep = Button.success(IDEnum.KEEP_BADWORD.key(), "Keep");
                        Button delete = Button.danger(IDEnum.DELETE_BADWORD.key(), "Delete");
                        Button ban = Button.danger(IDEnum.BAN_BADWORD.key(), "Ban");

                        if (channel != null) {
                            Message msg = channel.sendMessageEmbeds(embed.build()).setActionRow(keep, delete, ban).complete();
                            RedReaperBot.messageAssociation.addAssociation(e, msg);
                        } else if (channelPr != null) {
                            embed.addField("Server", e.getGuild().getName(), true);
                            Message msg = channelPr.sendMessageEmbeds(embed.build()).setActionRow(keep, delete, ban).complete();
                            RedReaperBot.messageAssociation.addAssociation(e, msg);
                        }
                        return;
                    }
                }
            }
        }
    }
}
