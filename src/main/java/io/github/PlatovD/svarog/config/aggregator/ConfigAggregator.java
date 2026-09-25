package io.github.PlatovD.svarog.config.aggregator;

import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;

@FunctionalInterface
public interface ConfigAggregator {

    void applyTo(SvarogConfigBuilder builder);

    default SvarogConfig load() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applyTo(builder);
        return builder.build();
    }
}