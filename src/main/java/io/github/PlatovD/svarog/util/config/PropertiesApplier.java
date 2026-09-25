package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.config.CircularDetection;
import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.definition.Scope;

import java.util.Properties;

public final class PropertiesApplier {

    public void apply(Properties props, SvarogConfigBuilder builder) {
        String pkg = props.getProperty(SvarogConfigKeys.SCAN_PACKAGE);
        if (pkg != null) {
            builder.scanPackage(pkg.trim());
        }

        String recursive = props.getProperty(SvarogConfigKeys.SCAN_RECURSIVE);
        if (recursive != null) {
            builder.scanRecursive(
                    ConfigValueParser.parseBoolean(recursive, SvarogConfigKeys.SCAN_RECURSIVE));
        }

        String scope = props.getProperty(SvarogConfigKeys.SCOPE_DEFAULT);
        if (scope != null) {
            builder.defaultScope(
                    ConfigValueParser.parseEnum(scope, Scope.class, SvarogConfigKeys.SCOPE_DEFAULT));
        }

        String lazy = props.getProperty(SvarogConfigKeys.LAZY);
        if (lazy != null) {
            builder.lazyInit(
                    ConfigValueParser.parseBoolean(lazy, SvarogConfigKeys.LAZY));
        }

        String circular = props.getProperty(SvarogConfigKeys.CIRCULAR_DETECTION);
        if (circular != null) {
            builder.circularDetection(
                    ConfigValueParser.parseEnum(
                            circular, CircularDetection.class,
                            SvarogConfigKeys.CIRCULAR_DETECTION));
        }

        String logLevel = props.getProperty(SvarogConfigKeys.LOGGING_LEVEL);
        if (logLevel != null) {
            builder.loggingLevel(
                    ConfigValueParser.parseEnum(
                            logLevel, LogLevel.class,
                            SvarogConfigKeys.LOGGING_LEVEL));
        }
    }
}
