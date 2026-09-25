package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.config.SvarogConfigKeys;

import java.util.Map;
import java.util.Properties;

public final class EnvPropertiesReader implements PropertiesReader {

    public static final String DEFAULT_PREFIX = SvarogConfigKeys.PREFIX_ENV;

    private final Map<String, String> env;
    private final String prefix;

    public EnvPropertiesReader() {
        this(System.getenv());
    }

    public EnvPropertiesReader(Map<String, String> env) {
        this(env, DEFAULT_PREFIX);
    }

    public EnvPropertiesReader(Map<String, String> env, String prefix) {
        if (env == null) {
            throw new IllegalArgumentException("env must not be null");
        }
        if (prefix == null) {
            throw new IllegalArgumentException("prefix must not be null");
        }
        this.env = env;
        this.prefix = prefix;
    }

    @Override
    public Properties read() {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(prefix)) {
                continue;
            }
            String suffix = key.substring(prefix.length());
            String propertyKey = SvarogConfigKeys.PREFIX + toPropertyKey(suffix);
            props.setProperty(propertyKey, entry.getValue());
        }
        return props;
    }

    private String toPropertyKey(String suffix) {
        return suffix.toLowerCase().replace('_', '.');
    }
}
