package com.github.redreaperlp.util;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandOptions {
    private final String name;
    private final String description;
    private final boolean forced;
    private final OptionType type;

    public CommandOptions(String name, String description, boolean forced, OptionType type) {
        this.name = name;
        this.description = description;
        this.forced = forced;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean forced() {
        return forced;
    }

    public OptionType type() {
        return type;
    }
}
