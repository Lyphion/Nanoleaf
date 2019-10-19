package me.lyphium.nanoleaf.api;

import lombok.Getter;

@Getter
public enum State {

    ACTIVE("on"),
    BRIGHTNESS("brightness"),
    HUE("hue"),
    SATURATION("sat"),
    COLOR_TEMPERATURE("ct"),
    COLOR_MODE("colorMode");

    //Key for Json
    private final String symbol;

    State(String symbol) {
        this.symbol = symbol;
    }

}
