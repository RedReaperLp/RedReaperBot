package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.Main;

public class SaveCaller implements Runnable{
    @Override
    public void run() {
        Main.servers.finalizer();
        System.out.println("Saved changes to storage.json");
        System.exit(0);
    }
}
