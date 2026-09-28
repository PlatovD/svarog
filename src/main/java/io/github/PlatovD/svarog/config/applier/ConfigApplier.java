package io.github.PlatovD.svarog.config.applier;

import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;

@FunctionalInterface
public interface ConfigApplier {
    void apply(ConfigDTO data, SvarogConfigBuilder builder);
}
