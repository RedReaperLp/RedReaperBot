package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.util.Servers;

import java.util.concurrent.TimeUnit;

public class FinalizerThread implements Runnable{
    private Servers object;

    public FinalizerThread(Servers object) {
        this.object = object;
    }
    @Override
    public void run() {
        while (true) {
            object.finalizer();
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
