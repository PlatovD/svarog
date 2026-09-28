package io.github.PlatovD.svarog.config.loader;

import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;

@FunctionalInterface
public interface ConfigLoader {

    void applyTo(SvarogConfigBuilder builder);

    default SvarogConfig load() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applyTo(builder);
        return builder.build();
    }
}