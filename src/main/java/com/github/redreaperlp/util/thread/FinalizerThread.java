package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.RedReaperBot;

import java.util.concurrent.TimeUnit;

public class FinalizerThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            RedReaperBot.servers.finalizer();
            RedReaperBot.usersettings.finalizer();
            RedReaperBot.authTokens.finalizer();
            try {
                TimeUnit.MINUTES.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
