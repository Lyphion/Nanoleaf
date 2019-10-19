package me.lyphium.nanoleaf.effect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class PluginProperty extends Property {

    public static final String[] PROPERTIES_NAMES = {
            "transTime", "loop", "linDirection", "radDirection",
            "rotDirection", "delayTime", "nColorsPerFrame", "mainColorProb"
    };

    public PluginProperty() {
        super(PROPERTIES_NAMES);
    }

    public static PluginProperty fromJson(JsonElement element) {
        final PluginProperty prop = new PluginProperty();
        final JsonArray arr = element.getAsJsonArray();

        final List<String> names = Arrays.asList(PROPERTIES_NAMES);

        for (JsonElement el : arr) {
            final JsonObject obj = el.getAsJsonObject();

            final String name = obj.get("name").getAsString();
            if (names.contains(name)) {
                final JsonPrimitive pri = obj.get("value").getAsJsonPrimitive();

                if (pri.isBoolean()) {
                    prop.properties.put(name, pri.getAsBoolean());
                } else if (pri.isNumber()) {
                    prop.properties.put(name, pri.getAsNumber());
                } else if (pri.isString()) {
                    prop.properties.put(name, pri.getAsString());
                }
            }
        }

        return prop;
    }

    public JsonArray toJson() {
        final JsonArray arr = new JsonArray();

        for (Entry<String, Object> entry : properties.entrySet()) {
            final JsonObject prop = new JsonObject();
            prop.addProperty("name", entry.getKey());
            final Object value = entry.getValue();

            if (value instanceof Number) {
                prop.addProperty("value", (Number) value);
            } else if (value instanceof Boolean) {
                prop.addProperty("value", (Boolean) value);
            } else {
                prop.addProperty("value", String.valueOf(value));
            }

            arr.add(prop);
        }
        return arr;
    }

    @Override
    public PluginProperty copy() {
        final PluginProperty property = new PluginProperty();

        for (Entry<String, Object> entry : properties.entrySet()) {
            property.setProperty(entry.getKey(), entry.getValue());
        }

        return property;
    }

}
