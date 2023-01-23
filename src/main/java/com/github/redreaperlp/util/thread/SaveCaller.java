package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.RedReaperBot;

public class SaveCaller implements Runnable{
    @Override
    public void run() {
        RedReaperBot.servers.finalizer();
        System.exit(0);
    }
}
