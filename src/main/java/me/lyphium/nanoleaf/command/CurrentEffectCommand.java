package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;

public class CurrentEffectCommand extends Command {

    public CurrentEffectCommand() {
        super("current", "Displays the current panel effect", "current");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length == 0) {
            System.out.println("Current effect: " + Main.API.getCurrentName());
            return true;
        }
        return false;
    }

}
