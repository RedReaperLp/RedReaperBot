package com.github.redreaperlp.networking;

import java.net.InetAddress;

public class Action {
    private final String input;
    InetAddress IP;

    public Action(String input) {
        this.input = input;
    }

    public String get(MessageAction action) {
        String[] split = input.split("#");
        for (String s : split) {
            String[] split1 = s.split("=");
            if (split1[0].equals(action.id() + "")) {
                return split1[1];
            }
        }
        return null;
    }

    public void IP(InetAddress IP) {
        this.IP = IP;
    }

    public InetAddress IP() {
        return IP;
    }
}
