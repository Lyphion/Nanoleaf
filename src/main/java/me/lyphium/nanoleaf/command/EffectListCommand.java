package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;

public class EffectListCommand extends Command {

    public EffectListCommand() {
        super("list", "Displays all available effects", "list");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length == 0) {
            System.out.println("Available effects: " + Main.API.getEffectsList());
            return true;
        }
        return false;
    }

}
