package com.github.redreaperlp.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable {

    private Action action = null;
    ServerSocket serverSocket = null;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(6000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                Socket receiver = serverSocket.accept();
                BufferedReader rs = new BufferedReader(new InputStreamReader(receiver.getInputStream()));
                String answer = rs.lines().toList().get(0);
                action = Codec.decode(answer);
                action.IP(receiver.getInetAddress());
                new ActionHandler(action);
                rs.close();
                receiver.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
