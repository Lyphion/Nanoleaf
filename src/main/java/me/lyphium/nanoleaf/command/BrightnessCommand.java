package me.lyphium.nanoleaf.command;

import me.lyphium.nanoleaf.Main;

public class BrightnessCommand extends Command {

    public BrightnessCommand() {
        super("brightness", "Change the brightness of the panels", "brightness [+/-][value]");
    }

    @Override
    public boolean execute(String label, String[] args) {
        if (args.length == 0) {
            System.out.println("Current brightness: " + Main.API.getBrightness());
            return true;
        } else if (args.length == 1) {
            final int value;
            if (args[0].startsWith("+") || args[0].startsWith("-")) {
                try {
                    final int temp = Integer.parseInt(args[0].substring(1));
                    final int old = Main.API.getBrightness();
                    value = Math.max(0, Math.min(100, args[0].startsWith("+") ? old + temp : old - temp));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number");
                    return true;
                }
            } else {
                try {
                    value = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number");
                    return true;
                }
                if (value < 0 || value > 100) {
                    System.err.println("Brightness must be between 0 and 100");
                }
            }
            Main.API.setBrightness(value);
            System.out.println("Brightness: " + value);
            return true;
        } else {
            return false;
        }
    }

}
