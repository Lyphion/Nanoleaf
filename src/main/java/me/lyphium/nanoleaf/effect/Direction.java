package me.lyphium.nanoleaf.effect;

public enum Direction {

    LEFT, RIGHT, UP, DOWN, OUTWARDS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
