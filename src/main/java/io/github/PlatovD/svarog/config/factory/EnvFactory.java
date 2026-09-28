package io.github.PlatovD.svarog.config.factory;

import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.config.reader.ConfigReader;
import io.github.PlatovD.svarog.config.reader.EnvReader;

public final class EnvFactory implements ConfigFactory {

    private final String prefix;

    public EnvFactory() {
        this(SvarogConfigKeys.PREFIX_ENV);
    }

    public EnvFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ConfigReader createReader() {
        return new EnvReader(System.getenv(), prefix);
    }
}
