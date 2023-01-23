package com.github.redreaperlp.enums;

import com.github.redreaperlp.util.CommandOptions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public enum CommandEn {


    /**
     * STATS <br>
     * - Key: stats <br>
     */
    STATS("stats", "Sends you a conclusion of Your Stats"),


    /**
     * CLEAR <br>
     * Key: clear <br>
     * - Key: amount <br>
     * - - - Type: Integer <br>
     * - - - Forced: true <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(0);} <br>
     * - Key: user <br>
     * - - - Type: User <br>
     * - - - Forced: false <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(1);} <br>
     * - Key: role <br>
     * - Type: Role <br>
     * - - - Forced: false <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(2);} <br>
     */
    CLEAR("clear", "Clears a specific number of Messages (of an specific User within the last x messages)",
            new CommandOptions("amount", "Clears a specified amount of Messages in current channel", true, OptionType.INTEGER), //Index 0
            new CommandOptions("user", "Clear only the provided Users messages within the specified amount", false, OptionType.USER), //Index 1
            new CommandOptions("role", "Clear only the provided Roles messages within the specified amount", false, OptionType.ROLE)), //Index 2


    /**
     * CHATPOINTS <br>
     * Key: chatpoints <br>
     */
    CHATPOINTS("chatpoints", "Displays you your Chat experience and Level with the Amount missing to level Up"),


    /**
     * BAN <br>
     * Key: ban <br>
     * - Key: user <br>
     * - - - Type: User <br>
     * - - - Forced: true <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(0);} <br>
     * - Key: reason <br>
     * - - - Type: String <br>
     * - - - Forced: true <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(1);} <br>
     * - Key: days <br>
     * - - - Type: Integer <br>
     * - - - Forced: false <br>
     * - - - Index: {@link #optionName(int) CommandEn.CLEAR.optionName(2);} <br>
     */
    BAN("ban", "Bans a mentioned User with an Reason",
            new CommandOptions("user", "The user, who should be banned", true, OptionType.USER), //Index 0
            new CommandOptions("reason", "The reason why the user is banned", true, OptionType.STRING), //Index 1
            new CommandOptions("days", "The amount of days the Messages from this User will be deleted", false, OptionType.INTEGER)), //Index 2
    /**
     * ROLES <br>
     * Key: roles
     */
    ROLES("roles","Creates a message where a Role selection Message can be requested"),
    BAD_WORDS_CHANNEL("bad-words-channel", "Here you can choose the Channel, where you get notified if someone uses blacklisted words",
            new CommandOptions("channel", "The channel where you get notified", false, OptionType.CHANNEL)),;

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
    public String optionName(int index) {
        return options[index].name();
    }
}
