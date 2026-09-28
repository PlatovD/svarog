package io.github.PlatovD.svarog.config.factory;

import io.github.PlatovD.svarog.config.reader.PropertiesReader;

public final class PropertiesFactory implements ConfigFactory {

    public static final String DEFAULT_FILE = "svarog.properties";

    private final String resourcePath;

    public PropertiesFactory() {
        this(DEFAULT_FILE);
    }

    public PropertiesFactory(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public PropertiesReader createReader() {
        return new PropertiesReader(resourcePath);
    }
}
