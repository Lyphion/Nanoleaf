package me.lyphium.nanoleaf.command;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    @Getter
    private final List<Command> commands = new ArrayList<>();

    public boolean addCommand(Command cmd) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                return false;
            }
        }
        return commands.add(cmd);
    }

    public boolean removeCommand(Command cmd) {
        return commands.remove(cmd);
    }

    public void callCommand(Command cmd, String label, String[] args) {
        final boolean result = cmd.execute(label, args);

        if (!result) {
            System.err.println("Please use '" + cmd.getUsage() + "'");
        }
    }

    public Command getCommand(String name) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }

        for (Command command : commands) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }

        return null;
    }

}
