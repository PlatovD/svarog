package io.github.PlatovD.svarog.config.reader;

import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.exception.SvarogConfigException;

import java.util.HashMap;
import java.util.Map;

public final class EnvReader implements ConfigReader {

    private final Map<String, String> env;
    private final String prefix;

    public EnvReader() {
        this(System.getenv());
    }

    public EnvReader(Map<String, String> env) {
        this(env, SvarogConfigKeys.PREFIX_ENV);
    }

    public EnvReader(Map<String, String> env, String prefix) {
        if (env == null) {
            throw new SvarogConfigException("env must not be null");
        }
        if (prefix == null) {
            throw new SvarogConfigException("prefix must not be null");
        }
        this.env = env;
        this.prefix = prefix;
    }

    @Override
    public ConfigDTO read() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(prefix)) {
                continue;
            }
            String suffix = key.substring(prefix.length());
            String propertyKey = SvarogConfigKeys.PREFIX + toPropertyKey(suffix);
            map.put(propertyKey, entry.getValue());
        }
        return new ConfigDTO(map);
    }

    private String toPropertyKey(String suffix) {
        return suffix.toLowerCase().replace('_', '.');
    }
}
