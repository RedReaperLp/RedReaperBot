package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

public class JMessageAssociation {

    public void checkValidity(Guild g) {
    }

    public JSONObject getAssociations(Guild g) {
        try {
            return RedReaperBot.servers.getGuild(g).getJSONObject(JsonSpecifier.MESSAGE_ASSOCIATION.key());
        } catch (JSONException e) {
            try {
                return RedReaperBot.servers.getGuild(g).put(JsonSpecifier.MESSAGE_ASSOCIATION.key(), new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public int getFreeIndex(Guild g) {
        int i = 0;
        while (getAssociations(g).has(String.valueOf(i))) {
            i++;
        }
        return i;
    }

    public void addAssociation(MessageReceivedEvent e, Message deleterMSG, boolean toOwner) {
        try {
            String key = deleterMSG.getId() + ":" + e.getMessageId();
            getAssociations(e.getGuild()).put(key,
                    new JSONObject()
                            .put(AssosiationsEn.DELETE_TARGET.key(), e.getMessageId())
                            .put(AssosiationsEn.DELETE_TARGET_CHANNEL.key(), e.getChannel().getId())
                            .put(AssosiationsEn.CHANNEL_STATUS.key(), toOwner ? "owner" : "channel")
            );
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean removeAssociation(Guild guild, MessageChannelUnion channel, Message space) {
        return false;
    }

    public String[] getAssociation(Guild guild, MessageChannelUnion channel, Message space) {
        
        return null;
    }
}