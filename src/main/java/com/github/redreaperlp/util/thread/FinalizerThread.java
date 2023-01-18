package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.util.storage.servers.Servers;

import java.util.concurrent.TimeUnit;

public class FinalizerThread extends Servers implements Runnable{
    @Override
    public void run() {
        while (true) {
            finalizer();
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
