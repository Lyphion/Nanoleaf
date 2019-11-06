package me.lyphium.nanoleaf;

import me.lyphium.nanoleaf.api.NanoleafAPI;
import me.lyphium.nanoleaf.command.*;
import me.lyphium.nanoleaf.effect.DisplayMirror;
import me.lyphium.nanoleaf.utils.PrettyPrintStream;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {

    public static final String IP = "192.168.1.3";
    public static final String TOKEN = "5ScPECdKzVsYbF54CCe5IV0ljGBTfq6C";

    public final static NanoleafAPI API = new NanoleafAPI(IP, TOKEN);
    public final static CommandManager MANAGER = new CommandManager();

    public static void main(String[] args) {
        System.setOut(new PrettyPrintStream(System.out, Level.INFO));
        System.setErr(new PrettyPrintStream(System.err, Level.WARNING));

        registerCommands();

        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            final String line = scanner.nextLine();

            if (line == null || line.trim().isEmpty())
                continue;
            final String[] split = line.split(" ");

            final String cmd = split[0];
            final String[] extra = Arrays.copyOfRange(split, 1, split.length);

            if (cmd.equalsIgnoreCase("exit")) {
                if (extra.length == 0) {
                    System.out.println("Programm exited");
                    break;
                } else {
                    System.err.println("Please use 'exit'");
                }
            }

            final Command command = MANAGER.getCommand(cmd);

            if (command == null) {
                System.err.println("Unknown Command. Please use 'help'");
            } else {
                MANAGER.callCommand(command, cmd, extra);
            }
        }

        DisplayMirror.stopMirror();
        final boolean active = API.isActive();
        API.setEffect("Forest");
        if (!active) {
            API.setActive(active);
        }
    }

    private static void registerCommands() {
        MANAGER.addCommand(new HelpCommand());
        MANAGER.addCommand(new ToggleCommand());
        MANAGER.addCommand(new BrightnessCommand());
        MANAGER.addCommand(new CurrentEffectCommand());
        MANAGER.addCommand(new EffectListCommand());
        MANAGER.addCommand(new SetEffectCommand());
        MANAGER.addCommand(new MirrorCommand());
    }

}