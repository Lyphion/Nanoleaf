package me.lyphium.nanoleaf.utils;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class PrettyPrintStream extends PrintStream {

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    private final Level level;

    public PrettyPrintStream(PrintStream stream, Level level) {
        super(stream);
        this.level = level;
    }

    @Override
    public void println(boolean x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(char x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(int x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(long x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(float x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(double x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(char[] x) {
        super.println(getPrefix() + new String(x));
    }

    @Override
    public void println(String x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println(Object x) {
        super.println(getPrefix() + x);
    }

    @Override
    public void println() {
        super.println(getPrefix());
    }

    private String getPrefix() {
        return "[" + format.format(new Date()) + " " + level.getName() + "]: ";
    }

}
