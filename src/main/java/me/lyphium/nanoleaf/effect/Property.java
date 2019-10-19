package me.lyphium.nanoleaf.effect;

import com.google.gson.JsonElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
public abstract class Property {

    private final String[] propertiesNames;

    public Property(String[] propertiesNames) {
        this.propertiesNames = propertiesNames;
    }

    @Getter
    protected final Map<String, Object> properties = new HashMap<>();

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
        final List<String> names = Arrays.asList(propertiesNames);
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

    public abstract JsonElement toJson();

    public abstract Property copy();

}
