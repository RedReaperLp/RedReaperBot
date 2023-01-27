package com.github.redreaperlp.json.storage.programs;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Programs {

    private File file;

    public JSONObject programs(Guild g) {
        return RedReaperBot.servers.getGuild(g);
    }

    public JSONObject getProgram(Guild g, int id) {
        try {
            return programs(g).getJSONObject(String.valueOf(id));
        } catch (JSONException e) {
            try {
                return programs(g).put(String.valueOf(id), new JSONObject());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String program(Guild g, int id) {
        try {
            return programs(g).getJSONObject(String.valueOf(id)).getString(JsonSpecifier.PROGRAM.key());
        } catch (JSONException e) {
            try {
                return programs(g).getJSONObject(String.valueOf(id)).put(JsonSpecifier.PROGRAM.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String path(Guild g, int id) {
        try {
            return programs(g).getJSONObject(String.valueOf(id)).getString(JsonSpecifier.PATH.key());
        } catch (JSONException e) {
            try {
                return programs(g).getJSONObject(String.valueOf(id)).put(JsonSpecifier.PATH.key(), "").toString();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
