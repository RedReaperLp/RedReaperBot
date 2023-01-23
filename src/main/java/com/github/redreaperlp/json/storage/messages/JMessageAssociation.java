package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.json.JSONException;

import static com.github.redreaperlp.enums.JsonSpecifier.MESSAGE_ASSOCIATION;

public class JMessageAssociation {
    public String[] getAssociation(Guild guild, Message message) {
        try {
            String temp = RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).getString(message.getId());
            System.out.println(temp);
            return temp.split(":"); // 0 = messageID, 1 = channelID
        } catch (JSONException e) {
            return null;
        }
    }

    public void addAssociation(Guild guild, Message identifier, Message associated, MessageChannelUnion associatedChannel) {
        try {
            RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).put(identifier.getId(), associated.getId() + ":" + associatedChannel.getId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeAssociation(Guild guild, Message message) {
        try {
            RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).remove(message.getId());
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
