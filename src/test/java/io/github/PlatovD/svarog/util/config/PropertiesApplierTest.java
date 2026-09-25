package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.config.CircularDetection;
import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesApplierTest {

    private final PropertiesApplier applier = new PropertiesApplier();

    @Test
    void apply_emptyProperties_leavesDefaults() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(new Properties(), builder);

        SvarogConfig config = builder.build();
        assertEquals("", config.getScanPackage());
        assertTrue(config.isScanRecursive());
        assertEquals(Scope.SINGLETON, config.getDefaultScope());
        assertFalse(config.isLazyInit());
        assertEquals(CircularDetection.STRICT, config.getCircularDetection());
        assertEquals(LogLevel.INFO, config.getLoggingLevel());
    }

    @Test
    void apply_scanPackage() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCAN_PACKAGE, "com.example.app");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertEquals("com.example.app", builder.build().getScanPackage());
    }

    @Test
    void apply_scanPackage_trims() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCAN_PACKAGE, "  com.example.app  ");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertEquals("com.example.app", builder.build().getScanPackage());
    }

    @Test
    void apply_scanRecursive() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCAN_RECURSIVE, "false");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertFalse(builder.build().isScanRecursive());
    }

    @Test
    void apply_defaultScope() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCOPE_DEFAULT, "PROTOTYPE");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertEquals(Scope.PROTOTYPE, builder.build().getDefaultScope());
    }

    @Test
    void apply_defaultScope_lowercase() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCOPE_DEFAULT, "prototype");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertEquals(Scope.PROTOTYPE, builder.build().getDefaultScope());
    }

    @Test
    void apply_lazy() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.LAZY, "true");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertTrue(builder.build().isLazyInit());
    }

    @Test
    void apply_loggingLevel() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        assertEquals(LogLevel.DEBUG, builder.build().getLoggingLevel());
    }

    @Test
    void apply_allProperties() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCAN_PACKAGE, "com.example.app");
        props.setProperty(SvarogConfigKeys.SCAN_RECURSIVE, "false");
        props.setProperty(SvarogConfigKeys.SCOPE_DEFAULT, "PROTOTYPE");
        props.setProperty(SvarogConfigKeys.LAZY, "true");
        props.setProperty(SvarogConfigKeys.CIRCULAR_DETECTION, "STRICT");
        props.setProperty(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        SvarogConfig config = builder.build();
        assertEquals("com.example.app", config.getScanPackage());
        assertFalse(config.isScanRecursive());
        assertEquals(Scope.PROTOTYPE, config.getDefaultScope());
        assertTrue(config.isLazyInit());
        assertEquals(CircularDetection.STRICT, config.getCircularDetection());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }

    @Test
    void apply_unknownKeys_ignored() {
        Properties props = new Properties();
        props.setProperty("svarog.unknown", "value");
        props.setProperty("other.key", "value");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(props, builder);

        SvarogConfig config = builder.build();
        assertEquals("", config.getScanPackage());
        assertFalse(config.isLazyInit());
    }

    @Test
    void apply_invalidBoolean_throws() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.LAZY, "yes");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();

        assertThrows(SvarogConfigException.class,
                () -> applier.apply(props, builder));
    }

    @Test
    void apply_invalidEnum_throws() {
        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.SCOPE_DEFAULT, "FOO");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();

        assertThrows(SvarogConfigException.class,
                () -> applier.apply(props, builder));
    }

    @Test
    void apply_doesNotOverrideMissingKeys() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder()
                .scanPackage("initial")
                .lazyInit(true);

        Properties props = new Properties();
        props.setProperty(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG");

        applier.apply(props, builder);

        SvarogConfig config = builder.build();
        assertEquals("initial", config.getScanPackage());
        assertTrue(config.isLazyInit());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }
}