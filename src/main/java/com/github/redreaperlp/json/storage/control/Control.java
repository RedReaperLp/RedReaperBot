package com.github.redreaperlp.json.storage.control;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

public class Control {
    public JSONObject control(Guild g) {
        try {
            return RedReaperBot.servers.getGuild(g).getJSONObject(JsonSpecifier.CONTROL.key());
        } catch (Exception e) {
            try {
                return RedReaperBot.servers.getGuild(g).put(JsonSpecifier.CONTROL.key(), new JSONObject());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public JSONObject control(String guild) {
        try {
            return RedReaperBot.servers.getGuild(guild).getJSONObject(JsonSpecifier.CONTROL.key());
        } catch (Exception e) {
            try {
                return RedReaperBot.servers.getGuild(guild).put(JsonSpecifier.CONTROL.key(), new JSONObject());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String auth(Guild g) {
        try {
            return control(g).getString("auth");
        } catch (Exception e) {
            try {
                return control(g).put("auth", "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public JSONObject controllable(Guild g) {
        try {
            return control(g).getJSONObject(JsonSpecifier.CONTROLLABLE.key());
        } catch (Exception e) {
            try {
                return control(g).put(JsonSpecifier.CONTROLLABLE.key(), new JSONObject());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
