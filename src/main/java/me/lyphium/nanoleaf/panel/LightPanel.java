package me.lyphium.nanoleaf.panel;

import lombok.Getter;

@Getter
public class LightPanel extends Panel {

    protected final int shapeType;

    public LightPanel(int id, double x, double y, int orientation, int shapeType) {
        super(id, x, y, orientation);
        this.shapeType = shapeType;
    }

    @Override
    public String toString() {
        return "LightPanel{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                ", shapeType=" + shapeType +
                '}';
    }

}
