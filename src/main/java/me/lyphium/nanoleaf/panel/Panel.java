package me.lyphium.nanoleaf.panel;

import lombok.Getter;

@Getter
public class Panel {

    protected final int id;
    protected final double x, y;
    protected final int orientation;

    public Panel(double x, double y) {
        this(-1, x, y, 0);
    }

    public Panel(int id, double x, double y, int orientation) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "LightPanel{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                '}';
    }

}
