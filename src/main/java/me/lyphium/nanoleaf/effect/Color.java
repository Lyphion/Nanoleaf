package me.lyphium.nanoleaf.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Random;

@Getter
@ToString
@EqualsAndHashCode
public final class Color {

    private final short hue, saturation, brightness;
    private final double probability;

    private Color(int hue, int saturation, int brightness, double probability) {
        if (hue < 0 || hue > 359)
            throw new IllegalArgumentException("Hue out of range (0-359)");
        if (saturation < 0 || saturation > 100)
            throw new IllegalArgumentException("Saturation out of range (0-100)");
        if (brightness < 0 || brightness > 100)
            throw new IllegalArgumentException("Brightness out of range (0-100)");
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Probability out of range (0-1)");

        this.hue = (short) hue;
        this.saturation = (short) saturation;
        this.brightness = (short) brightness;
        this.probability = probability;
    }

    public int getRed() {
        return getRGB().getRed();
    }

    public int getGreen() {
        return getRGB().getGreen();
    }

    public int getBlue() {
        return getRGB().getBlue();
    }

    public java.awt.Color getRGB() {
        return java.awt.Color.getHSBColor(hue / 360F, saturation / 100F, brightness / 100F);
    }

    public static Color fromHSB(int hue, int saturation, int brightness) {
        return fromHSB(hue, saturation, brightness, 0);
    }

    public static Color fromHSB(int hue, int saturation, int brightness, double probability) {
        return new Color(
                hue,
                saturation,
                brightness,
                probability
        );
    }

    public static Color fromRGB(int red, int green, int blue) {
        return fromRGB(red, green, blue, 0);
    }

    public static Color fromRGB(int red, int green, int blue, double probability) {
        final float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(red, green, blue, hsb);

        return new Color(
                (int) (hsb[0] * 360),
                (int) (hsb[1] * 100),
                (int) (hsb[2] * 100),
                probability
        );
    }

    public static Color fromColor(java.awt.Color other) {
        final float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(other.getRed(), other.getGreen(), other.getBlue(), hsb);

        return new Color(
                (int) (hsb[0] * 360),
                (int) (hsb[1] * 100),
                (int) (hsb[2] * 100),
                0
        );
    }

    public static Color fromJson(JsonElement element) {
        final JsonObject obj = element.getAsJsonObject();

        if (obj.has("hue")) {
            return fromHSB(
                    obj.get("hue").getAsInt(),
                    obj.get("saturation").getAsInt(),
                    obj.get("brightness").getAsInt(),
                    obj.get("probability").getAsDouble()
            );
        } else {
            return fromRGB(obj.get("red").getAsInt(),
                    obj.get("green").getAsInt(),
                    obj.get("blue").getAsInt(),
                    obj.get("probability").getAsDouble()
            );
        }
    }

    public static Color randomColor() {
        final Random random = new Random();
        return fromHSB(
                random.nextInt(360),
                random.nextInt(101),
                random.nextInt(101)
        );
    }

    public JsonObject toJson() {
        final JsonObject obj = new JsonObject();

        obj.addProperty("hue", hue);
        obj.addProperty("saturation", saturation);
        obj.addProperty("brightness", brightness);
        obj.addProperty("probability", probability);

        return obj;
    }

    public Color copy() {
        return new Color(hue, saturation, brightness, probability);
    }

}