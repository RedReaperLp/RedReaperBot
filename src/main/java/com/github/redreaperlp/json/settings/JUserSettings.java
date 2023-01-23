package com.github.redreaperlp.json.settings;

import com.github.redreaperlp.RedReaperBot;
import com.github.redreaperlp.util.JsonHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.github.redreaperlp.enums.ConfEnum.USER_SETTINGS;

public class JUserSettings {
    public static JSONObject userSettings;
    private File file = new File(RedReaperBot.conf.getConfig(USER_SETTINGS.key()));



    public JUserSettings() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                userSettings.put(USER_SETTINGS.key(), new JSONObject());
                JsonHelper.usersettingsChange();
                checkContain();
                finalizer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            userSettings = JsonHelper.resolver(file);
        }
    }

    public void checkContain() {
//        try {
//            if (!userSettings.has(USER_SETTINGS.key())) {
//                userSettings.put(USER_SETTINGS.key(), new JSONObject());
//                changes();
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void finalizer() {
        JsonHelper.usersettingsFinalizer(file, userSettings);
    }
    public void changes() {
        JsonHelper.usersettingsChange();
    }
}
