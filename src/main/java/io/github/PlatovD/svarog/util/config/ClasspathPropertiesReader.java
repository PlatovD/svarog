package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.exception.SvarogConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ClasspathPropertiesReader implements PropertiesReader {

    private final String resourcePath;

    public ClasspathPropertiesReader(String resourcePath) {
        if (resourcePath == null) {
            throw new SvarogConfigException("resourcePath must not be null");
        }
        this.resourcePath = resourcePath;
    }

    @Override
    public Properties read() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClasspathPropertiesReader.class.getClassLoader();
        }

        try (InputStream is = cl.getResourceAsStream(resourcePath)) {
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
            }
            return props;
        } catch (IOException e) {
            throw new SvarogConfigException(
                    "Failed to read " + resourcePath, e);
        }
    }
}
