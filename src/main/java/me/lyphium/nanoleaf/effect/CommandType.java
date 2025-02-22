package me.lyphium.nanoleaf.effect;

import lombok.Getter;

public enum CommandType {

    ADD("add"),
    RENAME("rename"),
    DELETE("delete"),
    REQUEST("request"),
    REQUEST_ALL("requestAll"),
    REQUEST_PLUGINS("requestPlugins"),
    DISPLAY("display"),
    DISPLAY_TEMP("displayTemp");

    @Getter
    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
