package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.RedReaperBot;

public class SaveCaller implements Runnable{
    @Override
    public void run() {
        RedReaperBot.servers.finalizer();
        RedReaperBot.usersettings.finalizer();
        RedReaperBot.authTokens.finalizer();
        System.exit(0);
    }
}
