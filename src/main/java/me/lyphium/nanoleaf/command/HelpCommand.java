package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Lists all commands and descriptions", "help");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        for (Command command : Main.MANAGER.getCommands()) {
            System.out.println(command.getUsage() + " | " + command.getDescription());
        }

        return true;
    }

}
