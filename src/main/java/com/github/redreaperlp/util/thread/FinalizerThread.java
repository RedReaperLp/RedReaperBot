package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.RedReaperBot;

import java.util.concurrent.TimeUnit;

public class FinalizerThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            RedReaperBot.servers.finalizer();
            RedReaperBot.usersettings.finalizer();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
