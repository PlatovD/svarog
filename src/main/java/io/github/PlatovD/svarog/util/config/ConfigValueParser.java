package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.exception.SvarogConfigException;

import java.util.Arrays;

public final class ConfigValueParser {

    private ConfigValueParser() {
    }

    public static boolean parseBoolean(String value, String key) {
        String v = value.trim().toLowerCase();
        if (!v.equals("true") && !v.equals("false")) {
            throw new SvarogConfigException(
                    "Invalid value for " + key + ": '" + value +
                            "'. Expected true or false.");
        }
        return Boolean.parseBoolean(v);
    }

    public static <E extends Enum<E>> E parseEnum(
            String value, Class<E> enumType, String key) {
        String v = value.trim().toUpperCase();
        try {
            return Enum.valueOf(enumType, v);
        } catch (IllegalArgumentException e) {
            throw new SvarogConfigException(
                    "Invalid value for " + key + ": '" + value +
                            "'. Allowed: " + Arrays.toString(enumType.getEnumConstants()));
        }
    }
}
