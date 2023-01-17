package com.github.redreaperlp.util.Stats;

import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import static com.github.redreaperlp.enums.JsonSpecifier.*;

public class ChatPoints {

    public void increment(JSONObject usersObj, User user, int amount) {
        try {
            int current = usersObj.getInt(STATS_CHATPOINT_POINTS.key());
            usersObj.put(STATS_CHATPOINT_POINTS.key(), current + amount);
        } catch (JSONException e) {
            System.out.println("Error while incrementing chatpoints for user " + user.getName());
        }
    }
    public void increment(JSONObject usersObj, User user) {
        increment(usersObj, user, 1);
    }

    public void decrement(JSONObject usersObj, User user, int amount) {
        try {
            int current = usersObj.getJSONObject(user.getId()).getInt(STATS_CHATPOINT_POINTS.key());
            usersObj.getJSONObject(user.getId()).put(STATS_CHATPOINT_POINTS.key(), current - amount);
        } catch (JSONException e) {
            System.out.println("Error while decrementing chatpoints for user " + user.getName());
        }
    }
    public void decrement(JSONObject usersObj, User user) {
        decrement(usersObj, user, 1);
    }

    public int[] calcExp(int chatpoints) {
        int level = 0;
        int pointCost = 10;
        while (chatpoints > pointCost) {
            level++;
            pointCost += level * 1.3;
            chatpoints -= pointCost;
        }
        return new int[]{level == 0 ? 1 : level, chatpoints};
    }
}
