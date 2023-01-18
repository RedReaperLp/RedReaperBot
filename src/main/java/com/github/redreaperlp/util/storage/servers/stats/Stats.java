package com.github.redreaperlp.util.storage.servers.stats;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.util.storage.servers.Servers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_LEVEL;
import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_POINTS;

public class Stats extends Servers {
    public JSONObject getStats(User user, Guild guild) {
        try {
            return Main.servers.getUser(user, guild).getJSONObject(user.getId()).getJSONObject("stats");
        } catch (JSONException e) {
            System.out.println("Error while getting stats for user " + user.getName() + "\n\n" + e.getMessage());
        }
        return null;
    }

    public void updateStats(JSONObject stats, int amount) {
        try {
            stats.put(STATS_CHATPOINTS_POINTS.key(), amount);
            stats.put(STATS_CHATPOINTS_LEVEL.key(), Main.chatPoints.calcExp(amount)[0]);
        } catch (JSONException e) {
            System.out.println("Error while updating stats for an user");
        }
    }
}
