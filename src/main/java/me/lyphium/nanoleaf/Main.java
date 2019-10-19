package me.lyphium.nanoleaf;

import me.lyphium.nanoleaf.api.NanoleafAPI;
import me.lyphium.nanoleaf.effect.DisplayMirror;
import me.lyphium.nanoleaf.exception.StatusCodeException;
import me.lyphium.nanoleaf.utils.PrettyPrintStream;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {

    public static final String IP = "192.168.1.3";
    public static final String TOKEN = "5ScPECdKzVsYbF54CCe5IV0ljGBTfq6C";

    public static void main(String[] args) {
        System.setOut(new PrettyPrintStream(System.out, Level.INFO));
        System.setErr(new PrettyPrintStream(System.err, Level.WARNING));

        final NanoleafAPI api = new NanoleafAPI(IP, TOKEN);
        DisplayMirror mirror = null;

        final Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit && scanner.hasNext()) {
            final String line = scanner.nextLine();

            if (line == null || line.trim().isEmpty())
                continue;
            final String[] split = line.split(" ");

            final String cmd = split[0];
            final String[] extra = Arrays.copyOfRange(split, 1, split.length);

            if (cmd.equalsIgnoreCase("exit")) {
                if (extra.length == 0) {
                    exit = true;
                    if (mirror != null) {
                        mirror.cancel();
                    }
                    System.out.println("Programm exited");
                } else {
                    System.err.println("Please use 'exit'");
                }
            } else if (cmd.equalsIgnoreCase("mirror")) {
                if (extra.length <= 1) {
                    if (mirror != null) {
                        mirror.cancel();
                    }
                    final int delay;
                    if (extra.length == 1) {
                        try {
                            delay = Integer.parseInt(extra[0]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number");
                            return;
                        }
                    } else {
                        delay = 200;
                    }

                    mirror = new DisplayMirror(api, delay);
                    mirror.start();
                    System.out.println("Mirror started");
                } else {
                    System.err.println("Please use 'mirror [delay]'");
                }
            } else if (cmd.equalsIgnoreCase("list")) {
                if (extra.length == 0) {
                    System.out.println("Available effects: " + api.getEffectsList());
                } else {
                    System.err.println("Please use 'list'");
                }
            } else if (cmd.equalsIgnoreCase("current")) {
                if (extra.length == 0) {
                    System.out.println("Current effect: " + api.getCurrentName());
                } else {
                    System.err.println("Please use 'current'");
                }
            } else if (cmd.equalsIgnoreCase("set")) {
                if (extra.length != 0) {
                    if (mirror != null) {
                        mirror.cancel();
                        mirror = null;
                    }
                    try {
                        api.setEffect(line.substring(4));
                        System.out.println("Effect set: " + api.getCurrentName());
                    } catch (StatusCodeException e) {
                        System.err.println("Couldn't set effect");
                    }
                } else {
                    System.err.println("Please use 'set <Name...>'");
                }
            } else if (cmd.equalsIgnoreCase("toggle")) {
                if (extra.length == 0) {
                    if (mirror != null) {
                        mirror.cancel();
                        mirror = null;
                    }
                    api.toggleActive();
                    System.out.println("Status: " + (api.isActive() ? "On" : "Off"));
                } else {
                    System.err.println("Please use 'toggle'");
                }
            } else if (cmd.equalsIgnoreCase("brightness")) {
                if (extra.length == 1) {
                    final int value;
                    if (extra[0].startsWith("+") || extra[0].startsWith("-")) {
                        try {
                            final int temp = Integer.parseInt(extra[0].substring(1));
                            final int old = api.getBrightness();
                            value = Math.max(0, Math.min(100, extra[0].startsWith("+") ? old + temp : old - temp));
                            System.out.println(value);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number");
                            return;
                        }
                    } else {
                        try {
                            value = Integer.parseInt(extra[0]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number");
                            return;
                        }
                        if (value < 0 || value > 100) {
                            System.err.println("Brightness must be between 0 and 100");
                            return;
                        }
                    }
                    api.setBrightness(value);
                    System.out.println("Brightness: " + value);
                } else {
                    System.out.println(api.getBrightness());
                }
            } else {
                System.err.println("Unknown command");
            }
        }
        final boolean active = api.isActive();
        api.setEffect("Forest");
        if (!active) {
            api.setActive(active);
        }
    }

}