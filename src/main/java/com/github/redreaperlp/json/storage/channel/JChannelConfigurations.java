package com.github.redreaperlp.json.storage.channel;

import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.RedReaperBot.servers;

public class JChannelConfigurations {

    public void setChannelConfigurations(Guild guild, ChannelConfigEn config, String value) {
        try {
            getChannelConfig(guild).put(config.key(), value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getChannelConfigurations(Guild guild, ChannelConfigEn config) {
        try {
            return getChannelConfig(guild).getString(config.key());
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getChannelConfig(Guild guild) {
        try {
            return servers.getGuild(guild).getJSONObject(JsonSpecifier.CHANNEL_CONFIG.key());
        } catch (JSONException e) {
            System.out.println("ChannelConfigurations not found, creating new one...");
            return new JSONObject();
        }
    }
}
