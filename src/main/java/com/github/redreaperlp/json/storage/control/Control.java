package com.github.redreaperlp.json.storage.control;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.enums.JsonSpecifier;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Control {
    public JSONObject control(Guild g) {
        return control(g.getId());
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
    public JSONObject controlable(Guild g, int id) {
        return controlable(g.getId(), id);
    }

    public JSONObject controlable(String guild, int id) {
        try {
            return control(guild).getJSONObject(String.valueOf(id));
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }

    public String program(Guild g, int id) {
        return program(g.getId(), id);
    }

    public String program(String guild, int id) {
        try {
            return controlable(guild, id).getString(JsonSpecifier.PROGRAM.key());
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }

    public JSONObject permissions(Guild g, int id) {
        return permissions(g.getId(), id);
    }

    public JSONObject permissions(String guild, int id) {
        try {
            return controlable(guild, id).getJSONObject(JsonSpecifier.PERMISSIONS.key());
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }

    public List<String> roles (Guild g, int id) {
        return roles(g.getId(), id);
    }

    public List<String> roles (String guild, int id) {
        try {
            return Arrays.stream(permissions(guild, id).getString(JsonSpecifier.PERMISSION_ROLES.key()).split("~")).toList();
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }

    public List<String> users (Guild g, int id) {
        return users(g.getId(), id);
    }

    public List<String> users (String guild, int id) {
        try {
            return Arrays.stream(permissions(guild, id).getString(JsonSpecifier.PERMISSION_USERS.key()).split("~")).toList();
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }

    public String ipAdress(Guild g, int id) {
        return ipAdress(g.getId(), id);
    }

    public String ipAdress(String guild, int id) {
        try {
            return controlable(guild, id).getString(JsonSpecifier.TARGET_IP.key());
        } catch (Exception e) {
            return null; //This only can be set through BotSender
        }
    }
}
