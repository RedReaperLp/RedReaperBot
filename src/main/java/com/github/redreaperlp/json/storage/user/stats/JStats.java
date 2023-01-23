package com.github.redreaperlp.json.storage.user.stats;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.json.storage.user.stats.util.JChatPoints;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_LEVEL;
import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_POINTS;

public class JStats {
    private JChatPoints chatPoints = new JChatPoints();

    public JSONObject getStats(User user, Guild guild) {
        try {
            return RedReaperBot.servers.user().getUser(user, guild).getJSONObject(user.getId()).getJSONObject("stats");
        } catch (JSONException e) {
            System.out.println("Error while getting stats for user " + user.getName() + "\n\n" + e.getMessage());
        }
        return null;
    }

    public void updateStats(JSONObject stats, int amount) {
        try {
            stats.put(STATS_CHATPOINTS_POINTS.key(), amount);
            stats.put(STATS_CHATPOINTS_LEVEL.key(), RedReaperBot.servers.chatPoints().calcExp(amount)[0]);
        } catch (JSONException e) {
            System.out.println("Error while updating stats for an user");
        }
    }

    public JChatPoints chatPoints() {
        return chatPoints;
    }
}
