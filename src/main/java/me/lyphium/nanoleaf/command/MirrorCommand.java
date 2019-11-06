package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;
import me.lyphium.nanoleaf.effect.DisplayMirror;

public class MirrorCommand extends Command {

    public MirrorCommand() {
        super("mirror", "Displays the first screen on the panels", "mirror [delay]");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length > 1) {
            return false;
        }
        DisplayMirror.stopMirror();

        final int delay;
        if (args.length == 1) {
            try {
                delay = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number");
                return true;
            }
        } else {
            delay = 200;
        }

        DisplayMirror.CURRENT_MIRROR = new DisplayMirror(Main.API, delay);
        DisplayMirror.CURRENT_MIRROR.start();

        System.out.println("Mirror started");

        return true;
    }

}
