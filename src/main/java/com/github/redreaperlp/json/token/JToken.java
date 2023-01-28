package com.github.redreaperlp.json.token;

import com.github.redreaperlp.util.JsonHelper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JToken {
    private File file = new File("tokens.json");
    private JSONObject tokens;

    public JToken() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                tokens = new JSONObject();
            } else {
                resolver();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addToken(Guild g, String token) {

    }

    public void resolver() {
        JsonHelper.resolver(file);
    }

    public void finalizer() {
        JsonHelper.tokenFinalizer(file, tokens);
    }

    public void changes() {
        JsonHelper.tokenChange();
    }
}
