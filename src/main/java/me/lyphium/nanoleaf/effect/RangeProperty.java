package me.lyphium.nanoleaf.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map.Entry;

public class RangeProperty extends Property {

    public static final String[] PROPERTIES_NAMES = {
            "minValue", "maxValue"
    };

    public RangeProperty() {
        super(PROPERTIES_NAMES);
    }

    public static RangeProperty fromJson(JsonElement element) {
        final RangeProperty prop = new RangeProperty();
        final JsonObject obj = element.getAsJsonObject();

        for (String property : PROPERTIES_NAMES) {
            if (obj.has(property)) {
                prop.properties.put(property, obj.getAsJsonPrimitive(property).getAsNumber());
            }
        }
        return prop;
    }

    @Override
    public JsonElement toJson() {
        final JsonObject obj = new JsonObject();

        for (Entry<String, Object> entry : properties.entrySet()) {
            obj.addProperty(entry.getKey(), (Number) entry.getValue());
        }
        return obj;
    }

    @Override
    public RangeProperty copy() {
        final RangeProperty property = new RangeProperty();

        for (Entry<String, Object> entry : properties.entrySet()) {
            property.properties.put(entry.getKey(), entry.getValue());
        }

        return property;
    }

}
