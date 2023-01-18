package com.github.redreaperlp.util.storage.servers.stats.util;

import com.github.redreaperlp.Main;
import com.github.redreaperlp.util.storage.servers.stats.Stats;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class ChatPoints extends Stats {

    public void increment(Guild guild, User user, int amount) {
        if (!user.isBot()) {
            try {
                JSONObject stats = getStats(user, guild);

                int current = stats.getInt(STATS_CHATPOINTS_POINTS.key()) + amount;
                updateStats(stats, current);
                changes();
            } catch (JSONException e) {
                System.out.println("Error while incrementing chatpoints for user " + user.getName());
                e.printStackTrace();
            }
        }
    }

    public void increment(Guild guild, User user) {
        increment(guild, user, 1);
    }

    public void decrement(Guild guild, User user, int amount) {
        if (!user.isBot()) {
            try {
                JSONObject stats = getStats(user, guild);
                int current = stats.getInt(STATS_CHATPOINTS_POINTS.key()) - amount;
                updateStats(stats, current);
                changes();
            } catch (JSONException e) {
                System.out.println("Error while decrementing chatpoints for user " + user.getName());
            }
        }
    }

    public void decrement(Guild guild, User user) {
        decrement(guild, user, 1);
    }

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
