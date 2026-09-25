package io.github.PlatovD.svarog.config.aggregator;

import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import io.github.PlatovD.svarog.util.config.ClasspathPropertiesReader;
import io.github.PlatovD.svarog.util.config.PropertiesApplier;
import io.github.PlatovD.svarog.util.config.PropertiesReader;

import java.util.Properties;

public final class PropertiesConfigAggregator implements ConfigAggregator {

    public static final String DEFAULT_FILE = "svarog.properties";

    private final PropertiesReader reader;
    private final PropertiesApplier applier;

    public PropertiesConfigAggregator() {
        this(DEFAULT_FILE);
    }

    public PropertiesConfigAggregator(String resourcePath) {
        this(new ClasspathPropertiesReader(resourcePath));
    }

    public PropertiesConfigAggregator(PropertiesReader reader) {
        if (reader == null) {
            throw new SvarogConfigException("reader must not be null");
        }
        this.reader = reader;
        this.applier = new PropertiesApplier();
    }

    @Override
    public void applyTo(SvarogConfigBuilder builder) {
        Properties props = reader.read();
        applier.apply(props, builder);
    }
}