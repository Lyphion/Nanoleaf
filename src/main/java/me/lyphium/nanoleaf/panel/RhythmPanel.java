package me.lyphium.nanoleaf.panel;

import lombok.Getter;

@Getter
public class RhythmPanel extends Panel {

    protected final int mode;

    public RhythmPanel(int id, double x, double y, int orientation, int mode) {
        super(id, x, y, orientation);
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "RhythmPanel{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                ", mode=" + mode +
                '}';
    }

}