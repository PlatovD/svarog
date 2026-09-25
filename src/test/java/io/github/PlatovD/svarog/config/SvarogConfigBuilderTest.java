package io.github.PlatovD.svarog.config;

import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SvarogConfigBuilderTest {

    @Test
    void defaults_scanPackage_empty() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertEquals("", config.getScanPackage());
    }

    @Test
    void defaults_scanRecursive_true() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertTrue(config.isScanRecursive());
    }

    @Test
    void defaults_defaultScope_singleton() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertEquals(Scope.SINGLETON, config.getDefaultScope());
    }

    @Test
    void defaults_lazyInit_false() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertFalse(config.isLazyInit());
    }

    @Test
    void defaults_circularDetection_strict() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertEquals(CircularDetection.STRICT, config.getCircularDetection());
    }

    @Test
    void defaults_loggingLevel_info() {
        SvarogConfig config = new SvarogConfigBuilder().build();
        assertEquals(LogLevel.INFO, config.getLoggingLevel());
    }

    @Test
    void setScanPackage() {
        SvarogConfig config = new SvarogConfigBuilder()
                .scanPackage("com.example.app")
                .build();
        assertEquals("com.example.app", config.getScanPackage());
    }

    @Test
    void setScanRecursive() {
        SvarogConfig config = new SvarogConfigBuilder()
                .scanRecursive(false)
                .build();
        assertFalse(config.isScanRecursive());
    }

    @Test
    void setDefaultScope() {
        SvarogConfig config = new SvarogConfigBuilder()
                .defaultScope(Scope.PROTOTYPE)
                .build();
        assertEquals(Scope.PROTOTYPE, config.getDefaultScope());
    }

    @Test
    void setLazyInit() {
        SvarogConfig config = new SvarogConfigBuilder()
                .lazyInit(true)
                .build();
        assertTrue(config.isLazyInit());
    }

    @Test
    void setLoggingLevel() {
        SvarogConfig config = new SvarogConfigBuilder()
                .loggingLevel(LogLevel.DEBUG)
                .build();
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }

    @Test
    void fluent_returnsSameBuilder() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        assertSame(builder, builder.scanPackage("x"));
        assertSame(builder, builder.scanRecursive(false));
        assertSame(builder, builder.defaultScope(Scope.PROTOTYPE));
        assertSame(builder, builder.lazyInit(true));
        assertSame(builder, builder.circularDetection(CircularDetection.STRICT));
        assertSame(builder, builder.loggingLevel(LogLevel.DEBUG));
    }

    @Test
    void scanPackage_null_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new SvarogConfigBuilder().scanPackage(null));
    }

    @Test
    void defaultScope_null_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new SvarogConfigBuilder().defaultScope(null));
    }

    @Test
    void circularDetection_null_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new SvarogConfigBuilder().circularDetection(null));
    }

    @Test
    void loggingLevel_null_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new SvarogConfigBuilder().loggingLevel(null));
    }
}