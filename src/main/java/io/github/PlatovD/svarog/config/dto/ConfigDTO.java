package io.github.PlatovD.svarog.config.dto;

import io.github.PlatovD.svarog.exception.SvarogConfigException;
import io.github.PlatovD.svarog.util.config.ConfigValueParser;

import java.util.Map;
import java.util.Set;

public final class ConfigDTO {

    private final Map<String, Object> values;

    public ConfigDTO(Map<String, Object> values) {
        this.values = values;
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    public String getString(String key) {
        Object value = values.get(key);
        return value == null ? null : String.valueOf(value);
    }

    public boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            throw new SvarogConfigException("Key not found: " + key);
        }
        return ConfigValueParser.parseBoolean(value, key);
    }

    public <E extends Enum<E>> E getEnum(String key, Class<E> type) {
        String value = getString(key);
        if (value == null) {
            throw new SvarogConfigException("Key not found: " + key);
        }
        return ConfigValueParser.parseEnum(value, type, key);
    }

    public Set<String> keys() {
        return values.keySet();
    }
}
