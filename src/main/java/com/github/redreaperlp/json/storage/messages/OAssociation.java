package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class OAssociation {
    private Message deleterMSG;
    private Message targetMSG;
    private String key;
    private Guild guild;
    private String associatedChannelID;
    private String associatedMSGID;

    public OAssociation(Message deleterMSG, String targetMessageChannelID, String targetMessageID) {
        this.deleterMSG = deleterMSG;
        key = deleterMSG.getId();
        try {
            this.targetMSG = RedReaperBot.jda
                    .getPrivateChannelById(targetMessageChannelID)
                    .retrieveMessageById(targetMessageID).complete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.targetMSG = RedReaperBot.jda
                    .getTextChannelById(targetMessageChannelID)
                    .retrieveMessageById(targetMessageID).complete();
            guild = targetMSG.getGuild();
        }
    }

    public OAssociation(Guild g, String key, String associatedChannelID, String associatedMSGID) {
        this.key = key;
        try {
            if (RedReaperBot.jda.getPrivateChannelById(associatedChannelID) != null) {
                this.targetMSG = RedReaperBot.jda
                        .getPrivateChannelById(associatedChannelID)
                        .retrieveMessageById(associatedMSGID).complete();
            } else {
                this.targetMSG = RedReaperBot.jda
                        .getTextChannelById(associatedChannelID)
                        .retrieveMessageById(associatedMSGID).complete();
            }
        } catch (Exception e) {
            //TODO: Remove this
            System.out.println(e.getMessage());
        }
        this.guild = g;

        String configuration = RedReaperBot.channelConfigurations.getChannelConfigurations(g, ChannelConfigEn.BAD_WORDS_ASK);
        try {
            if (configuration == null) {
                deleterMSG = g.getOwner().getUser().openPrivateChannel().complete().retrieveMessageById(key).complete();
            } else {
                deleterMSG = RedReaperBot.jda.getTextChannelById(configuration).retrieveMessageById(key).complete();
            }
        } catch (Exception e) {

        }
    }

    public Message deleterMSG() {
        return deleterMSG;
    }

    public Message targetMSG() {
        return targetMSG;
    }

    public String associatedChannelID() {
        return associatedChannelID;
    }

    public String associatedMSGID() {
        return associatedMSGID;
    }

    public String key() {
        return key;
    }
}
