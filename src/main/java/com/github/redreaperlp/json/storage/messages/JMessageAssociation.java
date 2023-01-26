package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JMessageAssociation {

    public JSONObject getAssociations(Guild g) {
        try {
            return RedReaperBot.servers.getGuild(g).getJSONObject(JsonSpecifier.MESSAGE_ASSOCIATION.key());
        } catch (JSONException e) {
            try {
                RedReaperBot.servers.getGuild(g).put(JsonSpecifier.MESSAGE_ASSOCIATION.key(), new JSONObject());
                return RedReaperBot.servers.getGuild(g).getJSONObject(JsonSpecifier.MESSAGE_ASSOCIATION.key());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Adds an association to the message association file (deleterMSGID:channelID:spaceID)
     *
     * @param e
     * @param deleterMSG
     * @throws JSONException if the file is not valid
     */
    public void addAssociation(MessageReceivedEvent e, Message deleterMSG) {
        try {
            getAssociations(e.getGuild()).put(deleterMSG.getId(), e.getMessage().getChannel().getId() + ":" + e.getMessage().getId());
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean removeAssociation(OAssociation association, Guild guild) {
        getAssociations(guild).remove(association.key());
        RedReaperBot.servers.changes();
        return true;
    }

    /**
     * Returns the association ids of the deleter message
     *
     * @param guild
     * @param deleterMsg
     * @return String[] ([0] = channelID of the associated Message, [1] = messageID of the associated Message)
     */
    public OAssociation getAssociation(Guild guild, Message deleterMsg) {
        try {
            String[] splitted = getAssociations(guild).getString(deleterMsg.getId()).split(":");
            return new OAssociation(deleterMsg, splitted[0], splitted[1]);
        } catch (JSONException e) {
            return null;
        }
    }

    public void checkValidity(Guild g) {
        List<OAssociation> toRemove = new ArrayList<>();
        try {
            Iterator iterator = getAssociations(g).keys();
            for (Iterator it = iterator; it.hasNext(); ) {
                String key = (String) it.next();
                String[] splitted = getAssociations(g).getString(key).split(":");
                OAssociation association = new OAssociation(g ,key, splitted[0], splitted[1]);
                if (association.targetMSG() == null) {
                    toRemove.add(association);
                    association.deleterMSG().delete().queue();
                } else {
                    String channelID = RedReaperBot.channelConfigurations.getChannelConfigurations(g, ChannelConfigEn.BAD_WORDS_ASK);
                    if (channelID == null) {
                        PrivateChannel channel = g.getOwner().getUser().openPrivateChannel().complete();
                        try {
                            channel.retrieveMessageById(key).complete();
                        } catch (Exception e) {
                            toRemove.add(association);
                        }
                    } else {
                        TextChannel channel = RedReaperBot.jda.getTextChannelById(channelID);
                        try {
                            channel.retrieveMessageById(key).complete();
                        } catch (Exception e) {
                            toRemove.add(association);
                        }
                    }
                }
            }
            for (OAssociation association : toRemove) {
                removeAssociation(association, g);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //ignore because it is not valid
        }
    }
}