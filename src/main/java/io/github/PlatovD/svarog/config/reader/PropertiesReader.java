package io.github.PlatovD.svarog.config.reader;

import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.exception.SvarogConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class PropertiesReader implements ConfigReader {

    private final String resourcePath;

    public PropertiesReader(String resourcePath) {
        if (resourcePath == null) {
            throw new SvarogConfigException("resourcePath must not be null");
        }
        this.resourcePath = resourcePath;
    }

    @Override
    public ConfigDTO read() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = PropertiesReader.class.getClassLoader();
        }

        try (InputStream is = cl.getResourceAsStream(resourcePath)) {
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
            }
            return toConfigDTO(props);
        } catch (IOException e) {
            throw new SvarogConfigException(
                    "Failed to read " + resourcePath, e);
        }
    }

    private ConfigDTO toConfigDTO(Properties props) {
        Map<String, Object> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return new ConfigDTO(map);
    }
}