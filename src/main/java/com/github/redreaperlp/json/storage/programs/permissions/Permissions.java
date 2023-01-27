package com.github.redreaperlp.json.storage.programs.permissions;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

public class Permissions {
    public JSONObject permissions(Guild g, int id) {
        try {
            return RedReaperBot.programs.getProgram(g, id).getJSONObject(JsonSpecifier.PERMISSIONS.key());
        } catch (JSONException e) {
            try {
                return RedReaperBot.programs.getProgram(g, id).put(JsonSpecifier.PERMISSIONS.key(), new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String user(Guild g, int id) {
        try {
            return permissions(g, id).getString(JsonSpecifier.USERS.key());
        } catch (JSONException e) {
            try {
                return permissions(g, id).put(JsonSpecifier.USERS.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String role(Guild g, int id) {
        try {
            return RedReaperBot.permissions.permissions(g, id).getString(JsonSpecifier.ROLES.key());
        } catch (JSONException e) {
            try {
                return RedReaperBot.permissions.permissions(g, id).put(JsonSpecifier.ROLES.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
