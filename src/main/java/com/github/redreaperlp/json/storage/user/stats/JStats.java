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
    private final JChatPoints chatPoints = new JChatPoints();

    public JSONObject getStats(User user, Guild guild) {
        try {
            return RedReaperBot.servers.user().getUser(user, guild).getJSONObject(user.getId()).getJSONObject("stats");
        } catch (JSONException e) {
        }
        return null;
    }

    public void updateStats(JSONObject stats, int amount) {
        try {
            stats.put(STATS_CHATPOINTS_POINTS.key(), amount);
            stats.put(STATS_CHATPOINTS_LEVEL.key(), RedReaperBot.chatPoints.calcExp(amount)[0]);
        } catch (JSONException e) {
        }
    }

    public JChatPoints chatPoints() {
        return chatPoints;
    }
}
