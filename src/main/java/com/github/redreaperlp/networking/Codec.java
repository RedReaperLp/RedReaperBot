package com.github.redreaperlp.networking;

public class Codec {
    public static String encode(EMessageAction... action) {
        String encoded = "";
        for (EMessageAction EMessageAction : action) {
            encoded += EMessageAction.id() + "=" + EMessageAction.value() + "#";
        }
        encoded = encoded.substring(0, encoded.length() - 1);
        return encoded;
    }

    public static Action decode(String input) {
        return new Action(input);
    }
}
