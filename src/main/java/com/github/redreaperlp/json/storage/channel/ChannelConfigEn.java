package com.github.redreaperlp.json.storage.channel;

public enum ChannelConfigEn {
    BAD_WORDS_ASK("BadWordsAsk"),

    ;

    private String key;

    ChannelConfigEn(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
