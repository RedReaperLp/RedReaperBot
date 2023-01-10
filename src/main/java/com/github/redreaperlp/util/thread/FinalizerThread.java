package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.Main;

import java.util.concurrent.TimeUnit;

public class FinalizerThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            System.out.println("Finalizing...");
            Main.servers.finalizer();
            System.out.println("Finalized!");
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
