package me.lyphium.nanoleaf.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import me.lyphium.nanoleaf.effect.AnimationType;
import me.lyphium.nanoleaf.effect.Color;
import me.lyphium.nanoleaf.effect.Effect;
import me.lyphium.nanoleaf.effect.EffectCommand;
import me.lyphium.nanoleaf.exception.StatusCodeException.*;
import me.lyphium.nanoleaf.panel.LightPanel;
import me.lyphium.nanoleaf.panel.Panel;
import me.lyphium.nanoleaf.panel.RhythmPanel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class NanoleafAPI {

    // https://github.com/rowak/nanoleaf-aurora/blob/master/src/io/github/rowak/nanoleafapi/Aurora.java
    // https://forum.nanoleaf.me/docs/openapi

    public static final short PORT = 16021;
    public static final String VERSION = "v1";

    public static final String URL_TEMPLATE = "http://%s:%d/api/%s/%s";

    @Getter
    private final String ip;
    private final String token;
    private final String basisUrl;

    @Getter
    private String name;
    @Getter
    private String serialNumber;
    @Getter
    private String manufacturer;
    @Getter
    private String firmwareVersion, hardwareVersion;
    @Getter
    private String model;

    public NanoleafAPI(String ip, String token) {
        this.ip = ip;
        this.token = token;
        this.basisUrl = String.format(URL_TEMPLATE, ip, PORT, VERSION, token);

        final JsonObject info = getAllInfo();

        this.name = info.get("name").getAsString();
        this.serialNumber = info.get("serialNo").getAsString();
        this.manufacturer = info.get("manufacturer").getAsString();
        this.firmwareVersion = info.get("firmwareVersion").getAsString();
        this.hardwareVersion = info.get("hardwareVersion").getAsString();
        this.model = info.get("model").getAsString();
    }

    public JsonObject getAllInfo() {
        final JsonElement element = executeGetRequest(basisUrl);
        return element.getAsJsonObject();
    }

    private JsonElement getStateInfo(State state) {
        return executeGetRequest(basisUrl + "/state/" + state.getSymbol());
    }

    public boolean isActive() {
        return getStateInfo(State.ACTIVE).getAsJsonObject().get("value").getAsBoolean();
    }

    public void setActive(boolean value) {
        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);

        final JsonObject data = new JsonObject();
        data.add("on", inner);

        executePutRequest(basisUrl + "/state", data);
    }

    public void toggleActive() {
        setActive(!isActive());
    }

    public byte getBrightness() {
        return getStateInfo(State.BRIGHTNESS).getAsJsonObject().get("value").getAsByte();
    }

    public byte getMinBrightness() {
        return getStateInfo(State.BRIGHTNESS).getAsJsonObject().get("min").getAsByte();
    }

    public byte getMaxBrightness() {
        return getStateInfo(State.BRIGHTNESS).getAsJsonObject().get("max").getAsByte();
    }

    public void setBrightness(int value) {
        setBrightness(value, 0);
    }

    public void increaseBrightness(int delta) {
        setBrightness(getBrightness() + delta, 0);
    }

    public void decreaseBrightness(int delta) {
        increaseBrightness(-delta);
    }

    public void setBrightness(int value, int duration) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Value must be between 0 and 100");
        }

        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);
        if (duration > 0) {
            inner.addProperty("duration", duration);
        }

        final JsonObject data = new JsonObject();
        data.add("brightness", inner);

        executePutRequest(basisUrl + "/state", data);
    }

    public byte getHue() {
        return getStateInfo(State.HUE).getAsJsonObject().get("value").getAsByte();
    }

    public byte getMinHue() {
        return getStateInfo(State.HUE).getAsJsonObject().get("min").getAsByte();
    }

    public byte getMaxHue() {
        return getStateInfo(State.HUE).getAsJsonObject().get("max").getAsByte();
    }

    public void setHue(int value) {
        if (value < 0 || value > 360) {
            throw new IllegalArgumentException("Value must be between 0 and 360");
        }

        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);

        final JsonObject data = new JsonObject();
        data.add("hue", inner);

        executePutRequest(basisUrl + "/state", data);
    }

    public byte getSaturation() {
        return getStateInfo(State.SATURATION).getAsJsonObject().get("value").getAsByte();
    }

    public byte getMinSaturation() {
        return getStateInfo(State.SATURATION).getAsJsonObject().get("min").getAsByte();
    }

    public byte getMaxSaturation() {
        return getStateInfo(State.SATURATION).getAsJsonObject().get("max").getAsByte();
    }

    public void setSaturation(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Value must be between 0 and 100");
        }

        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);

        final JsonObject data = new JsonObject();
        data.add("sat", inner);

        executePutRequest(basisUrl + "/state", data);
    }

    public int getColorTemperature() {
        return getStateInfo(State.COLOR_TEMPERATURE).getAsJsonObject().get("value").getAsInt();
    }

    public int getMinColorTemperature() {
        return getStateInfo(State.COLOR_TEMPERATURE).getAsJsonObject().get("min").getAsInt();
    }

    public int getMaxColorTemperature() {
        return getStateInfo(State.COLOR_TEMPERATURE).getAsJsonObject().get("max").getAsInt();
    }

    public void setColorTemperature(int value) {
        if (value < 1200 || value > 6500) {
            throw new IllegalArgumentException("Value must be between 1200 and 6500");
        }

        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);

        final JsonObject data = new JsonObject();
        data.add("ct", inner);

        executePutRequest(basisUrl + "/state", data);
    }

    public String getColorMode() {
        return getStateInfo(State.COLOR_MODE).getAsString();
    }

    public Color getColor() {
        return Color.fromHSB(getHue(), getSaturation(), getBrightness());
    }

    public void setColor(Color color) {
        setHue(color.getHue());
        setSaturation(color.getSaturation());
        setBrightness(color.getBrightness());
    }

    public String getCurrentName() {
        final JsonElement element = executeGetRequest(basisUrl + "/effects/select");
        return element.getAsString();
    }

    public void setEffect(String name) {
        final JsonObject data = new JsonObject();
        data.addProperty("select", name);

        executePutRequest(basisUrl + "/effects", data);
    }

    public void setRandomEffect() {
        final List<String> effects = getEffectsList();
        setEffect(effects.get(new Random().nextInt(effects.size())));
    }

    public List<String> getEffectsList() {
        final JsonElement element = executeGetRequest(basisUrl + "/effects/effectsList");

        final List<String> effects = new ArrayList<>();
        for (JsonElement eff : element.getAsJsonArray()) {
            effects.add(eff.getAsString());
        }
        return effects;
    }

    public Effect getEffect(String name) {
        final JsonObject inner = new JsonObject();
        inner.addProperty("command", "request");
        inner.addProperty("animName", name);

        final JsonObject data = new JsonObject();
        data.add("write", inner);

        final JsonElement element = executePutRequest(basisUrl + "/effects", data);
        return Effect.fromJson(element);
    }

    public List<Effect> getAllEffects() {
        final List<Effect> effects = new ArrayList<>();
        for (String s : getEffectsList()) {
            effects.add(getEffect(s));
        }
        return effects;
    }

    public void addEffect(Effect effect) {
        final JsonObject data = new JsonObject();
        data.add("write", effect.toJson(EffectCommand.ADD));

        executePutRequest(basisUrl + "/effects", data);
    }

    public void removeEffect(String name) {
        final JsonObject inner = new JsonObject();
        inner.addProperty("command", "delete");
        inner.addProperty("animName", name);

        final JsonObject data = new JsonObject();
        data.add("write", inner);

        executePutRequest(basisUrl + "/effects", data);
    }

    public void renameEffect(String name, String newName) {
        final JsonObject inner = new JsonObject();
        inner.addProperty("command", "rename");
        inner.addProperty("animName", name);
        inner.addProperty("newName", newName);

        final JsonObject data = new JsonObject();
        data.add("write", inner);

        executePutRequest(basisUrl + "/effects", data);
    }

    public void displayEffect(Effect effect) {
        final JsonObject data = new JsonObject();
        data.add("write", effect.toJson(EffectCommand.DISPLAY));

        executePutRequest(basisUrl + "/effects", data);
    }

    public void displayEffect(Effect effect, int duration) {
        final Effect copy = effect.copy();
        copy.setDuration(duration);

        final JsonObject data = new JsonObject();
        data.add("write", copy.toJson(EffectCommand.DISPLAY_TEMP));

        executePutRequest(basisUrl + "/effects", data);
    }

    public void displayEffect(String name, int duration) {
        final Effect effect = new Effect();
        effect.setName(name);
        effect.setDuration(duration);

        final JsonObject data = new JsonObject();
        data.add("write", effect.toJson(EffectCommand.DISPLAY_TEMP));

        executePutRequest(basisUrl + "/effects", data);
    }

    public void setPanelColor(LightPanel panel, Color color) {
        final Effect effect = new Effect();

        effect.setAnimationType(AnimationType.CUSTOM);
        effect.setAnimationData("1 " + panel.getId() + " 1 " +
                color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " 0 0");
        effect.setLooping(false);

        displayEffect(effect);
    }

    public void setPanelColor(LightPanel[] panels, Color[] colors) {
        final Effect effect = Effect.createStaticEffect("StaticColor", panels, colors, 0);
        effect.setAnimationType(AnimationType.CUSTOM);

        displayEffect(effect);
    }

    public void setPanelColor(List<LightPanel> panels, List<Color> colors) {
        setPanelColor(panels.toArray(new LightPanel[0]), colors.toArray(new Color[0]));
    }

    public void fadeToColor(Color color) {
        fadeToColor(color, 0);
    }

    public void fadeToColor(Color color, int transitionTime) {
        final LightPanel[] panels = getPanels().toArray(new LightPanel[0]);
        final Color[] colors = new Color[panels.length];
        Arrays.fill(colors, color);

        final Effect effect = Effect.createStaticEffect("StaticColor", panels, colors, transitionTime);
        effect.setAnimationType(AnimationType.CUSTOM);

        displayEffect(effect);
    }

    public short getGlobalOrientation() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/globalOrientation");
        return element.getAsJsonObject().get("value").getAsShort();
    }

    public short getMinGlobalOrientation() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/globalOrientation");
        return element.getAsJsonObject().get("min").getAsShort();
    }

    public short getMaxGlobalOrientation() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/globalOrientation");
        return element.getAsJsonObject().get("max").getAsShort();
    }

    public void setGlobalOrientation(int value) {
        if (value < 0 || value > 360) {
            throw new IllegalArgumentException("Value must be between 0 and 360");
        }

        final JsonObject inner = new JsonObject();
        inner.addProperty("value", value);

        final JsonObject data = new JsonObject();
        data.add("globalOrientation", inner);

        executePutRequest(basisUrl + "/panelLayout", data);
    }

    public int getPanelAmount() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/layout");
        return element.getAsJsonObject().get("numPanels").getAsInt();
    }

    public int getSideLength() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/layout");
        return element.getAsJsonObject().get("sideLength").getAsInt();
    }

    public List<LightPanel> getPanels() {
        final JsonElement element = executeGetRequest(basisUrl + "/panelLayout/layout");
        final JsonArray array = element.getAsJsonObject().getAsJsonArray("positionData");

        final List<LightPanel> panels = new ArrayList<>();
        for (JsonElement e : array) {
            final JsonObject panel = e.getAsJsonObject();
            panels.add(new LightPanel(
                    panel.get("panelId").getAsInt(),
                    panel.get("x").getAsDouble(),
                    panel.get("y").getAsDouble(),
                    panel.get("o").getAsInt(),
                    panel.get("shapeType").getAsInt()
            ));
        }
        return panels;
    }

    public List<LightPanel> getPanelsRotated() {
        final List<LightPanel> panels = getPanels();

        final Panel centroid = getCentroid(panels);
        final List<LightPanel> temp = new ArrayList<>();

        final int orientation = getGlobalOrientation() % 360;
        final double angle = Math.toRadians(orientation);

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);

        double minY = 0, maxX = 0;
        for (LightPanel p : panels) {
            final double x = p.getX() - centroid.getX();
            final double y = p.getY() - centroid.getY();

            final double newX = x * cos - y * sin;
            final double newY = x * sin + y * cos;

            temp.add(new LightPanel(
                    p.getId(),
                    newX,
                    newY,
                    p.getOrientation(),
                    p.getShapeType()
            ));

            if (newX > maxX) {
                maxX = newX;
            }
            if (newY < minY) {
                minY = newY;
            }
        }
        panels.clear();

        for (LightPanel p : temp) {
            panels.add(new LightPanel(
                    p.getId(),
                    maxX - p.getX(),
                    p.getY() - minY,
                    p.getOrientation(),
                    p.getShapeType()
            ));
        }

        return panels;
    }

    public Panel getPanel(int id) {
        for (LightPanel panel : getPanels()) {
            if (panel.getId() == id) {
                return panel;
            }
        }
        return null;
    }

    private Panel getCentroid(List<? extends Panel> panels) {
        int x = 0, y = 0;

        for (Panel p : panels) {
            x += p.getX();
            y += p.getY();
        }
        x /= panels.size();
        y /= panels.size();

        return new Panel(x, y);
    }

    public boolean hasRhythmPanel() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm/rhythmConnected");
        return element.getAsBoolean();
    }

    public boolean isRhythmActive() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm/rhythmActive");
        return element.getAsBoolean();
    }

    public boolean isAuxAvailable() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm/auxAvailable");
        return element.getAsBoolean();
    }

    public String getRhythmFirmwareVersion() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm/firmwareVersion");
        return element.getAsString();
    }

    public String getRhythmHardwareVersion() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm/hardwareVersion");
        return element.getAsString();
    }

    public RhythmPanel getRhythmPanel() {
        final JsonElement element = executeGetRequest(basisUrl + "/rhythm");
        final JsonObject rhythm = element.getAsJsonObject();

        if (rhythm.get("rhythmConnected").getAsBoolean()) {
            final JsonObject pos = rhythm.getAsJsonObject("rhythmPos");
            return new RhythmPanel(
                    rhythm.get("rhythmId").getAsInt(),
                    pos.get("x").getAsDouble(),
                    pos.get("y").getAsDouble(),
                    pos.get("o").getAsInt(),
                    rhythm.get("rhythmMode").getAsInt()
            );
        }
        return null;
    }

    public void setRhythmMode(int mode) {
        if (mode < 0 || mode > 1) {
            throw new IllegalArgumentException("Mode must be 0 or 1");
        }

        final JsonObject data = new JsonObject();
        data.addProperty("rhythmMode", mode);

        executePutRequest(basisUrl + "/rhythm/rhythmMode", data);
    }

    public void identify() {
        executePutRequest(basisUrl + "/identify", new JsonObject());
    }

    private JsonElement executeGetRequest(String url) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(500);
            con.setReadTimeout(500);

            checkResponseCode(con.getResponseCode());

            return readData(con);
        } catch (IOException e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            return null;
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private JsonElement executePostRequest(String url, JsonElement data) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(500);
            con.setReadTimeout(500);

            try (OutputStream outputStream = con.getOutputStream();
                 OutputStreamWriter stream = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(stream)) {
                writer.write(data.toString());
            }
            checkResponseCode(con.getResponseCode());

            return readData(con);
        } catch (IOException e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            return null;
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private JsonElement executePutRequest(String url, JsonElement data) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(500);
            con.setReadTimeout(500);

            try (OutputStream outputStream = con.getOutputStream();
                 OutputStreamWriter stream = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(stream)) {
                writer.write(data.toString());
            }
            checkResponseCode(con.getResponseCode());

            return readData(con);
        } catch (IOException e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            return null;
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private JsonElement readData(HttpURLConnection con) {
        final StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = con.getInputStream();
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader);
             JsonReader jsonReader = new JsonReader(reader)) {

            return JsonParser.parseReader(jsonReader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkResponseCode(int code) {
        switch (code) {
            case 400:
                throw new BadRequestException();
            case 401:
                throw new UnauthorizedException();
            case 403:
                throw new ForbiddenException();
            case 404:
                throw new PageNotFoundException();
            case 422:
                throw new UnprocessableEntityException();
            case 500:
                throw new InternalServerErrorException();
            default:
                break;
        }
    }

}
