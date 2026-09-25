package io.github.PlatovD.svarog.config;

public final class SvarogConfigKeys {

    public static final String PREFIX = "svarog.";
    public static final String PREFIX_ENV = "SVAROG_";

    public static final String SCAN_PACKAGE = PREFIX + "scan.package";
    public static final String SCAN_RECURSIVE = PREFIX + "scan.recursive";
    public static final String SCOPE_DEFAULT = PREFIX + "scope.default";
    public static final String LAZY = PREFIX + "lazy";
    public static final String CIRCULAR_DETECTION = PREFIX + "circular.detection";
    public static final String LOGGING_LEVEL = PREFIX + "logging.level";

    private SvarogConfigKeys() {
    }
}