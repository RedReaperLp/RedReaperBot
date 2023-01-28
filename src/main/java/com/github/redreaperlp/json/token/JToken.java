package com.github.redreaperlp.json.token;

import com.github.redreaperlp.util.JsonHelper;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

public class JToken {
    private File file = new File("tokens.json");
    private JSONObject tokens;

    public JToken() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                tokens = new JSONObject();
                changes();
            } else {
                resolver();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addToken(Guild g, String token) {
        try {
            tokens.put(String.valueOf(g.getIdLong()), token);
            changes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeToken(Guild g) {
        try {
            if (!tokens.has(String.valueOf(g.getIdLong()))) return;
            changes();
            tokens.remove(String.valueOf(g.getIdLong()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken(Guild g) {
        try {
            if (!tokens.has(String.valueOf(g.getIdLong()))) return null;
            return tokens.getString(String.valueOf(g.getIdLong()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getToken(String token) {
        try {
            for (Iterator it = tokens.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                if (tokens.getString(key).equals(token)) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void resolver() {
        this.tokens = JsonHelper.resolver(file);
    }

    public void finalizer() {
        JsonHelper.tokenFinalizer(file, tokens);
    }

    public void changes() {
        JsonHelper.tokenChange();
    }

    public String getGuildID(String auth) {
        try {
            for (Iterator it = tokens.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                if (tokens.getString(key).equals(auth)) return key;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
