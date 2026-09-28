package io.github.PlatovD.svarog.config.loader;

import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.applier.ConfigApplier;
import io.github.PlatovD.svarog.config.applier.DefaultConfigApplier;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.config.factory.ConfigFactory;
import io.github.PlatovD.svarog.config.reader.ConfigReader;

public final class DefaultConfigLoader implements ConfigLoader {

    private final ConfigReader reader;
    private final ConfigApplier applier = new DefaultConfigApplier();

    public DefaultConfigLoader(ConfigFactory factory) {
        this.reader = factory.createReader();
    }

    @Override
    public void applyTo(SvarogConfigBuilder builder) {
        ConfigDTO props = reader.read();
        applier.apply(props, builder);
    }
}
