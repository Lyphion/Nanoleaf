package me.lyphium.nanoleaf.effect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.EqualsAndHashCode;
import me.lyphium.nanoleaf.panel.LightPanel;

import java.util.*;
import java.util.Map.Entry;

@EqualsAndHashCode
public class Effect {

    public static final String[] PROPERTIES_NAMES = {
            "version", "duration", "animName", "newName", "animType", "animData", "colorType", "palette",
            "brightnessRange", "transTime", "delayTime", "flowFactor", "explodeFactor", "windowSize",
            "direction", "loop", "pluginType", "pluginOptions", "pluginUuid", "hasOverlay"
    };

    private final Map<String, Object> properties = new HashMap<>();

    public Effect() {}

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> clazz) {
        return (T) properties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, T defaultValue) {
        return hasProperty(key) ? (T) properties.get(key) : defaultValue;
    }

    public boolean setProperty(String key, Object value) {
        final List<String> names = Arrays.asList(PROPERTIES_NAMES);
        if (names.contains(key)) {
            properties.put(key, value);
            return true;
        }
        return false;
    }

    public boolean removeProperty(String key) {
        if (properties.containsKey(key)) {
            properties.remove(key);
            return true;
        }
        return false;
    }

    public String getVersion() {
        return getProperty("version", String.class);
    }

    public void setVersion(String version) {
        setProperty("version", version);
    }

    public int getDuration() {
        return getProperty("duration", -1);
    }

    public void setDuration(int duration) {
        setProperty("duration", duration);
    }

    public String getName() {
        return getProperty("animName", String.class);
    }

    public void setName(String name) {
        setProperty("animName", name);
    }

    public String getNewName() {
        return getProperty("newName", String.class);
    }

    public void setNewName(String newName) {
        setProperty("newName", newName);
    }

    public AnimationType getAnimationType() {
        final String type = getProperty("animType", String.class);
        if (type == null) {
            return null;
        }
        return AnimationType.valueOf(type.toUpperCase());
    }

    public void setAnimationType(AnimationType type) {
        setProperty("animType", type);
    }

    public String getAnimationData() {
        return getProperty("animData", String.class);
    }

    public void setAnimationData(String data) {
        setProperty("animData", data);
    }

    public String getColorType() {
        return getProperty("colorType", String.class);
    }

    public void setColorType(String type) {
        setProperty("colorType", type);
    }

    @SuppressWarnings("unchecked")
    public List<Color> getPalette() {
        return getProperty("palette", List.class);
    }

    public void setPalette(List<Color> palette) {
        setProperty("palette", palette);
    }

    public void addColor(Color color) {
        List<Color> palette = getPalette();
        if (palette == null) {
            setProperty("palette", palette = new ArrayList<>());
        }
        palette.add(color);
    }

    public void removeColor(Color color) {
        final List<Color> palette = getPalette();
        if (palette == null) {
            return;
        }
        palette.remove(color);
        if (palette.isEmpty()) {
            removeProperty("palette");
        }
    }

    public RangeProperty getBrightnessRange() {
        return getProperty("brightnessRange", RangeProperty.class);
    }

    public void setBrightnessRange(int min, int max) {
        final RangeProperty range = new RangeProperty();
        range.setProperty("minValue", min);
        range.setProperty("maxValue", max);

        setProperty("brightnessRange", range);
    }

    public int getMinBrightnessRange() {
        final RangeProperty range = getBrightnessRange();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("minValue", -1);
        }
    }

    public int getMaxBrightnessRange() {
        final RangeProperty range = getBrightnessRange();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("maxValue", -1);
        }
    }

    public RangeProperty getTransTime() {
        return getProperty("transTime", RangeProperty.class);
    }

    public void setTransTime(int min, int max) {
        final RangeProperty range = new RangeProperty();
        range.setProperty("minValue", min);
        range.setProperty("maxValue", max);

        setProperty("transTime", range);
    }

    public int getMinTransTime() {
        final RangeProperty range = getTransTime();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("minValue", -1);
        }
    }

    public int getMaxTransTime() {
        final RangeProperty range = getTransTime();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("maxValue", -1);
        }
    }

    public RangeProperty getDelayTime() {
        return getProperty("delayTime", RangeProperty.class);
    }

    public void setDelayTime(int min, int max) {
        final RangeProperty range = new RangeProperty();
        range.setProperty("minValue", min);
        range.setProperty("maxValue", max);

        setProperty("delayTime", range);
    }

    public int getMinDelayTime() {
        final RangeProperty range = getDelayTime();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("minValue", -1);
        }
    }

    public int getMaxDelayTime() {
        final RangeProperty range = getDelayTime();
        if (range == null) {
            return -1;
        } else {
            return range.getProperty("maxValue", -1);
        }
    }

    public int getFlowFactor() {
        return getProperty("flowFactor", -1);
    }

    public void setFlowFactor(int factor) {
        setProperty("flowFactor", factor);
    }

    public double getExplodeFactor() {
        return getProperty("explodeFactor", -1D);
    }

    public void setExplodeFactor(double factor) {
        setProperty("explodeFactor", factor);
    }

    public int getWindowSize() {
        return getProperty("windowSize", -1);
    }

    public void setWindowSize(int size) {
        setProperty("windowSize", size);
    }

    public Direction getDirection() {
        final String type = getProperty("direction", String.class);
        if (type == null) {
            return null;
        }
        return Direction.valueOf(type.toUpperCase());
    }

    public void setDirection(Direction dir) {
        setProperty("direction", dir);
    }

    public boolean isLooping() {
        return getProperty("loop", false);
    }

    public void setLooping(boolean looping) {
        setProperty("loop", looping);
    }

    public String getPluginType() {
        return getProperty("pluginType", String.class);
    }

    public void setPluginType(String type) {
        setProperty("pluginType", type);
    }

    public PluginProperty getPluginOptions() {
        return getProperty("pluginOptions", PluginProperty.class);
    }

    public void setPluginOptions(PluginProperty property) {
        setProperty("pluginOptions", property);
    }

    public Object getPluginOption(String name) {
        final PluginProperty property = getPluginOptions();
        if (property == null) {
            return null;
        }
        return property.getProperty(name, Object.class);
    }

    public boolean setPluginOption(String name, Object value) {
        PluginProperty property = getPluginOptions();
        if (property == null) {
            setProperty("pluginOptions", property = new PluginProperty());
        }
        return property.setProperty(name, value);
    }

    public UUID getPluginUUID() {
        final String uuid = getProperty("pluginUuid", String.class);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    public void setPluginUUID(UUID uuid) {
        setProperty("pluginUuid", uuid.toString());
    }

    public boolean hasOverlay() {
        return getProperty("hasOverlay", false);
    }

    public void setOverlay(boolean overlay) {
        setProperty("hasOverlay", overlay);
    }

    public static Effect createStaticEffect(String name, LightPanel[] panels, Color[] colors) {
        return createStaticEffect(name, panels, colors, 0);
    }

    public static Effect createStaticEffect(String name, LightPanel[] panels, Color[] colors, int transitiontime) {
        final Effect effect = new Effect();

        effect.setName(name);
        effect.setAnimationType(AnimationType.STATIC);
        effect.setLooping(false);

        final StringBuilder builder = new StringBuilder();
        builder.append(panels.length);

        for (int i = 0; i < panels.length; i++) {
            final LightPanel panel = panels[i];
            final java.awt.Color color = colors[i].getRGB();

            builder.append(" ");
            builder.append(panel.getId());                  //Panelid
            builder.append(" 1 ");                          //Framenr
            builder.append(color.getRed()).append(" ");     //Red
            builder.append(color.getGreen()).append(" ");   //Green
            builder.append(color.getBlue()).append(" ");    //Blue
            builder.append("0 ").append(transitiontime);    //Transitiontime
        }

        effect.setAnimationData(builder.toString());

        return effect;
    }

    public static Effect createStaticEffect(String name, LightPanel[] panels, java.awt.Color[] colors, int transitiontime) {
        final Effect effect = new Effect();

        effect.setName(name);
        effect.setAnimationType(AnimationType.STATIC);
        effect.setLooping(false);

        final StringBuilder builder = new StringBuilder();
        builder.append(panels.length);

        for (int i = 0; i < panels.length; i++) {
            final LightPanel panel = panels[i];
            final java.awt.Color color = colors[i];

            builder.append(" ");
            builder.append(panel.getId());                  //Panelid
            builder.append(" 1 ");                          //Framenr
            builder.append(color.getRed()).append(" ");     //Red
            builder.append(color.getGreen()).append(" ");   //Green
            builder.append(color.getBlue()).append(" ");    //Blue
            builder.append("0 ").append(transitiontime);    //Transitiontime
        }

        effect.setAnimationData(builder.toString());

        return effect;
    }

    public static Effect createStaticEffect(String name, String data) {
        final Effect effect = new Effect();

        effect.setName(name);
        effect.setAnimationType(AnimationType.STATIC);
        effect.setAnimationData(data);
        effect.setLooping(false);

        return effect;
    }

    public static Effect createCustomEffect(String name, String data, boolean looping) {
        final Effect effect = new Effect();

        effect.setName(name);
        effect.setAnimationType(AnimationType.CUSTOM);
        effect.setAnimationData(data);
        effect.setLooping(looping);

        return effect;
    }

    public static Effect fromJson(JsonElement element) {
        final Effect effect = new Effect();
        final JsonObject obj = element.getAsJsonObject();

        for (String property : PROPERTIES_NAMES) {
            if (obj.has(property)) {
                if (property.equals("palette")) {
                    final List<Color> palette = new ArrayList<>();
                    for (JsonElement color : obj.getAsJsonArray(property)) {
                        palette.add(Color.fromJson(color));
                    }
                    effect.properties.put(property, palette);
                } else if (property.equals("pluginOptions")) {
                    final JsonElement options = obj.get(property);
                    effect.properties.put(property, PluginProperty.fromJson(options));
                } else if (obj.get(property).isJsonObject()) {
                    final JsonElement options = obj.get(property);
                    effect.properties.put(property, RangeProperty.fromJson(options));
                } else if (obj.get(property).isJsonPrimitive()) {
                    final JsonPrimitive pri = obj.get(property).getAsJsonPrimitive();

                    if (pri.isBoolean()) {
                        effect.properties.put(property, pri.getAsBoolean());
                    } else if (pri.isNumber()) {
                        effect.properties.put(property, pri.getAsNumber());
                    } else if (pri.isString()) {
                        effect.properties.put(property, pri.getAsString());
                    }
                }
            }
        }

        return effect;
    }

    public JsonObject toJson(CommandType command) {
        final JsonObject obj = new JsonObject();

        if (command != null) {
            obj.addProperty("command", command.toString());
        }

        for (Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() instanceof Number) {
                obj.addProperty(entry.getKey(), (Number) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                obj.addProperty(entry.getKey(), (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof Property) {
                obj.add(entry.getKey(), ((Property) entry.getValue()).toJson());
            } else if (entry.getValue() instanceof List) {
                final List list = (List) entry.getValue();
                final JsonArray arr = new JsonArray();
                for (Object o : list) {
                    if (o instanceof Color) {
                        arr.add(((Color) o).toJson());
                    } else {
                        arr.add(o.toString());
                    }
                }
                obj.add(entry.getKey(), arr);
            } else {
                obj.addProperty(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        return obj;
    }

    @Override
    public String toString() {
        return toJson(null).toString();
    }

    @SuppressWarnings("unchecked")
    public Effect copy() {
        final Effect effect = new Effect();

        for (Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() instanceof Property) {
                effect.setProperty(entry.getKey(), ((Property) entry.getValue()).copy());
            } else if (entry.getValue() instanceof List) {
                final List list = new ArrayList();
                for (Object o : (List) entry.getValue()) {
                    if (o instanceof Color) {
                        list.add(((Color) o).copy());
                    } else {
                        list.add(o);
                    }
                }
                effect.setProperty(entry.getKey(), list);
            } else {
                effect.setProperty(entry.getKey(), entry.getValue());
            }
        }

        return effect;
    }

}