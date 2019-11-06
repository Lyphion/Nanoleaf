package me.lyphium.nanoleaf.command;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public abstract class Command {

    private final String name;
    private final String[] aliases;

    private final String description;
    private final String usage;

    public Command(String name, String description, String usage) {
        this(name, Collections.emptyList(), description, usage);
    }

    public Command(String name, List<String> aliases, String description, String usage) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name must not be null!");

        this.name = name.trim();
        this.aliases = aliases == null ? new String[0] : aliases.toArray(new String[0]);
        this.description = description;
        this.usage = usage;
    }

    public abstract boolean execute(String label, String[] args);

}
