package com.github.redreaperlp.commands;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.commands.handlers.ChatPoints;
import com.github.redreaperlp.commands.message.MessageHandleEnum;
import com.github.redreaperlp.commands.message.MessageHandler;
import com.github.redreaperlp.enums.CommandEn;
import com.github.redreaperlp.enums.IDEnum;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import com.github.redreaperlp.json.storage.messages.OAssociation;
import com.github.redreaperlp.networking.Codec;
import com.github.redreaperlp.networking.EMessageAction;
import com.github.redreaperlp.networking.Sender;
import com.github.redreaperlp.util.thread.DeleterThread;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
                setBadWordsChannel(e);
            } else if (e.getName().equals(CommandEn.ADD_BAD_WORD.key())) {
                RedReaperBot.badMessages.addBadMessage(e);
            } else if (e.getName().equals(CommandEn.REMOVE_BAD_WORD.key())) {
                RedReaperBot.badMessages.removeBadMessage(e);
            } else if (e.getName().equals(CommandEn.GENERATE_AUTH_TOKEN.key())) {
                generateToken(e);
            } else if (e.getName().equals(CommandEn.PANEL.key())) {
                panel(e);
            }
        }
    }

    private void panel(SlashCommandInteractionEvent e) {
        int conrolableID = 0;
        try {
            conrolableID = e.getOption("id").getAsInt();
            if (hasPermission(e.getGuild(), conrolableID, e.getMember())) {
                MessageEmbed eb = new EmbedBuilder()
                        .setTitle(RedReaperBot.control.program(e.getGuild(), conrolableID))
                        .setImage("https://cdn.discordapp.com/attachments/1060601917878829226/1069017159877009438/PanelBanner.png")
                        .addField("Control Panel", "This is the control panel for the program " + RedReaperBot.control.program(e.getGuild(), conrolableID), false)
                        .addField("Start", "Click button to start the program", true)
                        .addField("Stop", "Click button to stop the program", true)
                        .addField("Logs", "Click button to see the logs", false)
                        .addField("Status", "Click button to see the status", false)
                        .addField("Restart", "Click button to restart the program", false)
                        .setThumbnail(e.getGuild().getIconUrl())
                        .setColor(0xff00).build();
                e.replyEmbeds(eb).addActionRow(
                        Button.primary(IDEnum.START.key() + "-" + conrolableID, IDEnum.START.key()),
                        Button.danger(IDEnum.STOP.key() + "-" + conrolableID, IDEnum.STOP.key()),
                        Button.secondary(IDEnum.LOG.key() + "-" + conrolableID, IDEnum.LOG.key()),
                        Button.secondary(IDEnum.STATUS.key() + "-" + conrolableID, IDEnum.STATUS.key()),
                        Button.secondary(IDEnum.RESTART.key() + "-" + conrolableID, IDEnum.RESTART.key())
                ).queue();
            } else {
                e.reply("You dont have permission to use this panel!").setEphemeral(true).queue();
            }
        } catch (Exception ex) {
            //TODO: send all panel ids if user has permission
        }

    }

    public boolean hasPermission(Guild g, int conrolableID, Member member) {
        boolean hasPermission = false;
        List<String> users = RedReaperBot.control.users(g, conrolableID);
        if (users != null) {
            if (users.contains(member.getId())) {
                hasPermission = true;
            }
        }
        if (!hasPermission) {
            List<String> roles = RedReaperBot.control.roles(g, conrolableID);
            if (roles != null) {
                List<Role> userRoles = member.getRoles();
                for (Role role : userRoles) {
                    if (roles.contains(role.getId())) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }
        return hasPermission;
    }

    private void generateToken(SlashCommandInteractionEvent e) {
        String token = RedReaperBot.generateRandomString(15);
        RedReaperBot.authTokens.removeToken(e.getGuild());
        RedReaperBot.authTokens.addToken(e.getGuild(), token);
        MessageEmbed eb = new EmbedBuilder().setTitle("Auth Token")
                .addField("Token", token, false)
                .setImage("https://cdn.discordapp.com/attachments/1060601917878829226/1069007091341217892/YourTokenBanner.png")
                .addField("Be Careful!", "Keep it good, once Generated you cant show it again", false)
                .setThumbnail(e.getGuild().getIconUrl())
                .setColor(0xff00).build();
        e.replyEmbeds(eb).setEphemeral(true).queue();
    }

    private void setBadWordsChannel(SlashCommandInteractionEvent e) {
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
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        String id = e.getComponentId();
        String idSplit[] = id.split("-");
        id = idSplit[0];
        System.out.println("Button: " + id);
        Message space = e.getMessage();
        IDEnum idEnum = IDEnum.fromKey(id);
        Guild guild = guildByEmbed(e);
        switch (idEnum) {
            case DELETE_BADWORD -> {
                OAssociation association = RedReaperBot.messageAssociation.getAssociation(guild, space);
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
                OAssociation association = RedReaperBot.messageAssociation.getAssociation(guild, space);
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
            case START -> {
                if (hasPermission(e.getGuild(), Integer.parseInt(idSplit[1]), e.getMember())) {
                    e.deferReply().setEphemeral(true).queue();
                    String conrolableID = idSplit[1];
                    EMessageAction.CONTROL_ID.value(conrolableID + "*1");
                    String sendable = Codec.encode(EMessageAction.CONTROL_ID);
                    String[] ip = RedReaperBot.control.ipAdress(e.getGuild(), Integer.parseInt(conrolableID)).split(":");
                    new Sender(ip[0], Integer.parseInt(ip[1]), sendable, e);
                } else {
                    e.reply("You dont have the permission to do this!").setEphemeral(true).queue();
                }
            }
            case STOP -> {
                if (hasPermission(e.getGuild(), Integer.parseInt(idSplit[1]), e.getMember())) {
                    e.deferReply().setEphemeral(true).queue();
                    String conrolableID = idSplit[1];
                    EMessageAction.CONTROL_ID.value(conrolableID + "*2");
                    String sendable = Codec.encode(EMessageAction.CONTROL_ID);
                    String[] ip = RedReaperBot.control.ipAdress(e.getGuild(), Integer.parseInt(conrolableID)).split(":");
                    new Sender(ip[0], Integer.parseInt(ip[1]), sendable, e);
                } else {
                    e.reply("You dont have the permission to do this!").setEphemeral(true).queue();
                }
            }
            case LOG -> {
                if (hasPermission(e.getGuild(), Integer.parseInt(idSplit[1]), e.getMember())) {
                    e.deferReply().setEphemeral(true).queue();
                    String conrolableID = idSplit[1];
                    EMessageAction.CONTROL_ID.value(conrolableID + "*3");
                    String sendable = Codec.encode(EMessageAction.CONTROL_ID);
                    String[] ip = RedReaperBot.control.ipAdress(e.getGuild(), Integer.parseInt(conrolableID)).split(":");
                    new Sender(ip[0], Integer.parseInt(ip[1]), sendable, e);
                } else {
                    e.reply("You dont have the permission to do this!").setEphemeral(true).queue();
                }
            }
            case STATUS -> {
                if (hasPermission(e.getGuild(), Integer.parseInt(idSplit[1]), e.getMember())) {
                    e.deferReply().setEphemeral(true).queue();
                    String conrolableID = idSplit[1];
                    EMessageAction.CONTROL_ID.value(conrolableID + "*4");
                    String sendable = Codec.encode(EMessageAction.CONTROL_ID);
                    String[] ip = RedReaperBot.control.ipAdress(e.getGuild(), Integer.parseInt(conrolableID)).split(":");
                    new Sender(ip[0], Integer.parseInt(ip[1]), sendable, e);
                } else {
                    e.reply("You dont have the permission to do this!").setEphemeral(true).queue();
                }
            }
            case RESTART -> {
                if (hasPermission(e.getGuild(), Integer.parseInt(idSplit[1]), e.getMember())) {
                    e.deferReply().setEphemeral(true).queue();
                    String conrolableID = idSplit[1];
                    EMessageAction.CONTROL_ID.value(conrolableID + "*5");
                    String sendable = Codec.encode(EMessageAction.CONTROL_ID);
                    String[] ip = RedReaperBot.control.ipAdress(e.getGuild(), Integer.parseInt(conrolableID)).split(":");
                    new Sender(ip[0], Integer.parseInt(ip[1]), sendable, e);
                } else {
                    e.reply("You dont have the permission to do this!").setEphemeral(true).queue();
                }
            }
        }
    }


    /**
     * Gets the guild from the embed
     *
     * @param e
     * @return the guild of the embed or null if not found
     * @note The message must have an embed with a field named #IDEnum.CHANNEL.key() with the channel id
     */
    public Guild guildByEmbed(ButtonInteractionEvent e) {
        GuildMessageChannel channel;
        for (MessageEmbed.Field field : e.getMessage().getEmbeds().get(0).getFields()) {
            if (field.getName().equals(IDEnum.CHANNEL.key())) {
                channel = RedReaperBot.jda.getTextChannelById(field.getValue().replace("<#", "").replace(">", ""));
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

