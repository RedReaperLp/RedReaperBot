package com.github.redreaperlp.networking;

public class Codec {
    public static String encode(MessageAction... action) {
        String encoded = "";
        for (MessageAction messageAction : action) {
            encoded += messageAction.id() + "=" + messageAction.value() + "#";
        }
        encoded = encoded.substring(0, encoded.length() - 1);
        return encoded;
    }

    public static Action decode(String input) {
        return new Action(input);
    }
}
