package io.github.PlatovD.svarog.config;

import io.github.PlatovD.svarog.definition.Scope;

public final class SvarogConfig {

    private final String scanPackage;
    private final boolean scanRecursive;
    private final Scope defaultScope;
    private final boolean lazyInit;
    private final CircularDetection circularDetection;
    private final LogLevel loggingLevel;

    SvarogConfig(
            String scanPackage,
            boolean scanRecursive,
            Scope defaultScope,
            boolean lazyInit,
            CircularDetection circularDetection,
            LogLevel loggingLevel) {
        this.scanPackage = scanPackage;
        this.scanRecursive = scanRecursive;
        this.defaultScope = defaultScope;
        this.lazyInit = lazyInit;
        this.circularDetection = circularDetection;
        this.loggingLevel = loggingLevel;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public boolean isScanRecursive() {
        return scanRecursive;
    }

    public Scope getDefaultScope() {
        return defaultScope;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public CircularDetection getCircularDetection() {
        return circularDetection;
    }

    public LogLevel getLoggingLevel() {
        return loggingLevel;
    }

    @Override
    public String toString() {
        return "SvarogConfig{" +
                "scanPackage='" + scanPackage + '\'' +
                ", scanRecursive=" + scanRecursive +
                ", defaultScope=" + defaultScope +
                ", lazyInit=" + lazyInit +
                ", circularDetection=" + circularDetection +
                ", loggingLevel=" + loggingLevel +
                '}';
    }
}