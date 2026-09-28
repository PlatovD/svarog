package io.github.PlatovD.svarog.config.applier;

import io.github.PlatovD.svarog.config.CircularDetection;
import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.definition.Scope;

public final class DefaultConfigApplier implements ConfigApplier {

    public void apply(ConfigDTO data, SvarogConfigBuilder builder) {
        if (data.has(SvarogConfigKeys.SCAN_PACKAGE)) {
            builder.scanPackage(data.getString(SvarogConfigKeys.SCAN_PACKAGE).trim());
        }

        if (data.has(SvarogConfigKeys.SCAN_RECURSIVE)) {
            builder.scanRecursive(data.getBoolean(SvarogConfigKeys.SCAN_RECURSIVE));
        }

        if (data.has(SvarogConfigKeys.SCOPE_DEFAULT)) {
            builder.defaultScope(
                    data.getEnum(SvarogConfigKeys.SCOPE_DEFAULT, Scope.class));
        }

        if (data.has(SvarogConfigKeys.LAZY)) {
            builder.lazyInit(data.getBoolean(SvarogConfigKeys.LAZY));
        }

        if (data.has(SvarogConfigKeys.CIRCULAR_DETECTION)) {
            builder.circularDetection(
                    data.getEnum(SvarogConfigKeys.CIRCULAR_DETECTION, CircularDetection.class));
        }

        if (data.has(SvarogConfigKeys.LOGGING_LEVEL)) {
            builder.loggingLevel(
                    data.getEnum(SvarogConfigKeys.LOGGING_LEVEL, LogLevel.class));
        }
    }
}
