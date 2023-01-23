package com.github.redreaperlp.json.storage.messages;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.channel.ChannelConfigEn;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.redreaperlp.enums.JsonSpecifier.MESSAGE_ASSOCIATION;

public class JMessageAssociation {
    public String[] getAssociation(Guild guild, Message message) {
        try {
            String temp = RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).getString(message.getId());
            return temp.split(":"); // 0 = messageID, 1 = channelID
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Adds an association to the JSON file
     *
     * @param guild             The guild the association is in
     * @param identifier        The message that is used as an identifier (identifier: "associated:channel")
     * @param associated        The message that is associated with the identifier (associated:)
     * @param associatedChannel The channel of the associated message (:channel)
     */
    public void addAssociation(Guild guild, Message identifier, Message associated, MessageChannelUnion associatedChannel) {
        try {
            if (!RedReaperBot.servers.getGuild(guild).has(MESSAGE_ASSOCIATION.key())) {
                RedReaperBot.servers.getGuild(guild).put(MESSAGE_ASSOCIATION.key(), new JSONObject());
            }
            RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).put(identifier.getId(), associated.getId() + ":" + associatedChannel.getId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeAssociation(Guild guild, Message identifier) {
        return removeAssociation(guild, identifier.getId());
    }

    public boolean removeAssociation(Guild guild, String identifier) {
        try {
            TextChannel channel = RedReaperBot.jda.getTextChannelById(RedReaperBot.channelConfigurations.getChannelConfigurations(guild, ChannelConfigEn.BAD_WORDS_ASK));
            try {
                Message message = channel.retrieveMessageById(identifier).complete();
                message.delete().queue();
            } catch (Exception e) {
            } // Message was already deleted
            RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key()).remove(identifier);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public void checkValidity(Guild guild) {
        try {
            JSONObject obj = RedReaperBot.servers.getGuild(guild).getJSONObject(MESSAGE_ASSOCIATION.key());
            List<String> keysToRemove = new ArrayList<>();
            Iterator<String> i = obj.keys();
            while (i.hasNext()) {
                String key = i.next();
                String[] temp = obj.getString(key).split(":");
                try {
                    RedReaperBot.jda.getTextChannelById(temp[1]).retrieveMessageById(temp[0]).complete();
                    RedReaperBot.jda
                            .getTextChannelById(RedReaperBot.channelConfigurations.getChannelConfigurations(guild, ChannelConfigEn.BAD_WORDS_ASK))
                            .retrieveMessageById(key).complete();
                } catch (Exception e) {
                    keysToRemove.add(key);

                }
            }
            for (String key : keysToRemove) {
                removeAssociation(guild, key);
            }
            if (keysToRemove.size() > 0) {
                RedReaperBot.servers.changes();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
