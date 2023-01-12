package com.github.redreaperlp.enums;

import com.github.redreaperlp.util.CommandOptions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public enum CommandEn {
    STATS("stats", "Sends you a conclusion of Your Stats"),
    CLEAR("clear", "Clears a specific number of Messages (of an specific User within the last x messages)",
            new CommandOptions("amount", "Clears a specified amount of Messages in current channel", true, OptionType.INTEGER), //Index 0
            new CommandOptions("user", "Clear only the provided Users messages within the specified amount", false, OptionType.USER)), //Index 1
    CHATPOINTS("chatpoints", "Displays you your Chat experience and Level with the Amount missing to level Up"),
    BAN("ban", "Bans a mentioned User with an Reason",
            new CommandOptions("user", "The user, who should be banned", true, OptionType.USER), //Index 0
            new CommandOptions("reason", "The reason why the user is banned", true, OptionType.STRING), //Index 1
            new CommandOptions("days", "The amount of days the Messages from this User will be deleted", false, OptionType.INTEGER)); //Index 2

    private String key;
    private String description;
    private CommandOptions[] options = null;

    CommandEn(String key, String description, CommandOptions... options) {
        this.key = key;
        this.description = description;
        this.options = options;
    }

    CommandEn(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String key() {
        return this.key;
    }

    public String description() {
        return this.description;
    }

    public CommandOptions[] options() {
        return options;
    }
}
