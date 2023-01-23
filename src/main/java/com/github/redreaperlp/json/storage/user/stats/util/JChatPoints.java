package com.github.redreaperlp.json.storage.user.stats.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.RedReaperBot.servers;
import static com.github.redreaperlp.enums.JsonSpecifier.STATS_CHATPOINTS_POINTS;

public class JChatPoints {

    /**
     * Increments the chatpoints for the given user
     * @param guild
     * @param user
     * @param amount
     * @return true if the user leveled up, false if not
     */
    public boolean increment(Guild guild, User user, int amount) {
        if (!user.isBot()) {
            try {
                JSONObject stats = servers.stats().getStats(user, guild);

                int current = stats.getInt(STATS_CHATPOINTS_POINTS.key()) + amount;
                servers.stats().updateStats(stats, current);
                servers.changes();
                return calcExp(current)[0] != calcExp(current - amount)[0];
            } catch (JSONException e) {
                System.out.println("Error while incrementing chatpoints for user " + user.getName());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean increment(Guild guild, User user) {
        return increment(guild, user, 1);
    }

    public void decrement(Guild guild, User user, int amount) {
        if (!user.isBot()) {
            try {
                JSONObject stats = servers.stats().getStats(user, guild);
                int current = stats.getInt(STATS_CHATPOINTS_POINTS.key()) - amount;
                servers.stats().updateStats(stats, current);
                servers.changes();
            } catch (JSONException e) {
                System.out.println("Error while decrementing chatpoints for user " + user.getName());
            }
        }
    }

    public void decrement(Guild guild, User user) {
        decrement(guild, user, 1);
    }

    /**
     * Calculates the level and remaining chatpoints for the given amount of chatpoints
     * @param chatpoints the amount of chatpoints (int)
     * @return an array with the level and remaining chatpoints [0] = level, [1] = remaining
     */
    public int[] calcExp(int chatpoints) {
        int level = 0;
        int remaining = 0;
        int cost = 10;

        while (chatpoints >= cost) {
            chatpoints -= cost;
            cost *= 1.3;
            level++;
        }

        if (level <= 0) {
            level = 1;
            remaining = cost - chatpoints;
        } else {
            remaining = cost - chatpoints;
        }
        int[] arr = new int[]{level, remaining};
        return arr; // [0] = level, [1] = remaining
    }

}
