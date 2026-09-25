package io.github.PlatovD.svarog.config;

import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogConfigException;

public final class SvarogConfigBuilder {

    private String scanPackage = "";
    private boolean scanRecursive = true;
    private Scope defaultScope = Scope.SINGLETON;
    private boolean lazyInit = false;
    private CircularDetection circularDetection = CircularDetection.STRICT;
    private LogLevel loggingLevel = LogLevel.INFO;

    public SvarogConfigBuilder scanPackage(String scanPackage) {
        if (scanPackage == null) {
            throw new SvarogConfigException("scanPackage must not be null");
        }
        this.scanPackage = scanPackage;
        return this;
    }

    public SvarogConfigBuilder scanRecursive(boolean scanRecursive) {
        this.scanRecursive = scanRecursive;
        return this;
    }

    public SvarogConfigBuilder defaultScope(Scope defaultScope) {
        if (defaultScope == null) {
            throw new SvarogConfigException("defaultScope must not be null");
        }
        this.defaultScope = defaultScope;
        return this;
    }

    public SvarogConfigBuilder lazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
        return this;
    }

    public SvarogConfigBuilder circularDetection(CircularDetection circularDetection) {
        if (circularDetection == null) {
            throw new SvarogConfigException("circularDetection must not be null");
        }
        this.circularDetection = circularDetection;
        return this;
    }

    public SvarogConfigBuilder loggingLevel(LogLevel loggingLevel) {
        if (loggingLevel == null) {
            throw new SvarogConfigException("loggingLevel must not be null");
        }
        this.loggingLevel = loggingLevel;
        return this;
    }

    public SvarogConfig build() {
        return new SvarogConfig(
                scanPackage,
                scanRecursive,
                defaultScope,
                lazyInit,
                circularDetection,
                loggingLevel
        );
    }
}
