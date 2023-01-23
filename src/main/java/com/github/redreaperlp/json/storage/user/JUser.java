package com.github.redreaperlp.json.storage.user;

import com.github.redreaperlp.enums.JsonSpecifier;
import com.github.redreaperlp.json.storage.user.stats.JStats;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.RedReaperBot.servers;
import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class JUser {
    private JStats stats = new JStats();

    public void removeUser(User user, Guild guild) {
        try {
            servers.storageObj.getJSONObject(STORAGE.key())
                    .getJSONObject(GUILD.key())
                    .getJSONObject(guild.getId()).remove(user.getId());
            servers.changes();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsUser(User user, Guild server, JsonSpecifier object) {
        try {
            switch (object) {
                case STATS -> {
                    return getUser(user, server).getJSONObject(STATS.key()) != null;
                }
                case NAME -> {
                    return getUser(user, server).getString(NAME.key()) != null;
                }
                case STATS_CHATPOINTS_POINTS -> {
                    return getUser(user, server).getJSONObject(STATS.key()).getInt(STATS_CHATPOINTS_POINTS.key()) != 0;
                }
                case STATS_CHATPOINTS_LEVEL -> {
                    return getUser(user, server).getJSONObject(STATS.key()).getInt(STATS_CHATPOINTS_LEVEL.key()) != 0;
                }
                default -> {
                    return false;
                }
            }
        } catch (JSONException e) {
            return false;
        }
    }

    public JSONObject getUsers(Guild guild) {
        try {
            JSONObject usr = servers.getGuild(guild).getJSONObject(USERS.key());
            if (usr == null) {
                servers.changes();
                return servers.getGuild(guild).put(USERS.key(), new JSONObject());
            }
            return usr;
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getUser(User user, Guild guild) {
        try {
            JSONObject usr = getUsers(guild).getJSONObject(user.getId());
            servers.getGuild(guild).getJSONObject(USERS.key()).put(user.getId(), usr);
            return new JSONObject().put(user.getId(), usr);
        } catch (JSONException e) {
            try {
                JSONObject usr = new JSONObject().put(STATS.key(), new JSONObject()
                                .put(STATS_CHATPOINTS_POINTS.key(), 0)
                                .put(STATS_CHATPOINTS_LEVEL.key(), 0))
                        .put(NAME.key(), user.getName());
                servers.getGuild(guild).getJSONObject(USERS.key()).put(user.getId(), usr);
                return new JSONObject().put(user.getId(), usr);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public JStats stats() {
        return stats;
    }
}
