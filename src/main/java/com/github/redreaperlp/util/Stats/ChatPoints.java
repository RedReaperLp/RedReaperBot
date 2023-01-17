package com.github.redreaperlp.util.Stats;

import com.github.redreaperlp.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class ChatPoints {

    public void increment(Guild guild, User user, int amount) {
        try {
            JSONObject stats = Main.servers.getUser(user, guild)
                    .getJSONObject(user.getId())
                    .getJSONObject(STATS.key());
            int current = stats.getInt(STATS_CHATPOINTS_POINTS.key()) + amount;
            stats.put(STATS_CHATPOINTS_POINTS.key(), current);
            stats.put(STATS_CHATPOINTS_LEVEL.key(), calcExp(current)[0]);
            Main.servers.changes();
        } catch (JSONException e) {
            System.out.println("Error while incrementing chatpoints for user " + user.getName());
            e.printStackTrace();
        }
    }

    public void increment(Guild guild, User user) {
        increment(guild, user, 1);
    }

    public void decrement(Guild guild, User user, int amount) {
        JSONObject usersObj = Main.servers.getUser(user, guild);
        try {
            int current = usersObj.getJSONObject(user.getId()).getInt(STATS_CHATPOINTS_POINTS.key());
            usersObj.getJSONObject(user.getId()).put(STATS_CHATPOINTS_POINTS.key(), current - amount);
            usersObj.getJSONObject(user.getId()).put(STATS_CHATPOINTS_LEVEL.key(), calcExp(current)[1]);
            Main.servers.changes();
        } catch (JSONException e) {
            System.out.println("Error while decrementing chatpoints for user " + user.getName());
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
