package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;
import me.lyphium.nanoleaf.effect.DisplayMirror;
import me.lyphium.nanoleaf.exception.StatusCodeException;

public class SetEffectCommand extends Command {

    public SetEffectCommand() {
        super("set", "Set a new panel effect", "set <Name...>");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        DisplayMirror.stopMirror();

        final String line = String.join(" ", args);

        try {
            Main.API.setEffect(line);
            System.out.println("Effect set: " + Main.API.getCurrentName());
        } catch (StatusCodeException e) {
            System.err.println("Couldn't set effect");
        }
        return true;
    }

}
