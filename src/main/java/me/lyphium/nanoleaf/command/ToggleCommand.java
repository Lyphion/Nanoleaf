package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;
import me.lyphium.nanoleaf.effect.DisplayMirror;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "Toggles the panels on/off", "toggle");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        DisplayMirror.stopMirror();

        Main.API.toggleActive();
        System.out.println("Status: " + (Main.API.isActive() ? "On" : "Off"));

        return true;
    }

}