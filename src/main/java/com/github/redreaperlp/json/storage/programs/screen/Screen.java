package com.github.redreaperlp.json.storage.programs.screen;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

public class Screen {
    public JSONObject screen(Guild g, int id) {
        try {
            return RedReaperBot.programs.getProgram(g, id).getJSONObject(JsonSpecifier.SCREEN.key());
        } catch (JSONException e) {
            try {
                return RedReaperBot.programs.getProgram(g, id).put(JsonSpecifier.SCREEN.key(), new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String name(Guild g, int id) {
        try {
            return screen(g, id).getString(JsonSpecifier.NAME.key());
        } catch (JSONException e) {
            try {
                return screen(g, id).put(JsonSpecifier.NAME.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String stop(Guild g, int id) {
        try {
            return screen(g, id).getString(JsonSpecifier.STOP.key());
        } catch (JSONException e) {
            try {
                return screen(g, id).put(JsonSpecifier.STOP.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String start(Guild g, int id) {
        try {
            return screen(g, id).getString(JsonSpecifier.START.key());
        } catch (JSONException e) {
            try {
                return screen(g, id).put(JsonSpecifier.START.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
