package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.Main;

public class SaveCaller implements Runnable{
    @Override
    public void run() {
        Main.conf.saveConfig();
        System.out.println("Saved config");
        System.exit(0);
    }
}
