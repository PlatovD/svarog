package io.github.PlatovD.svarog.config.applier;

import io.github.PlatovD.svarog.config.CircularDetection;
import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConfigApplierTest {

    private final DefaultConfigApplier applier = new DefaultConfigApplier();

    private static ConfigDTO dto(Map<String, Object> values) {
        return new ConfigDTO(values);
    }

    private static ConfigDTO dto(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return new ConfigDTO(map);
    }

    @Test
    void apply_emptyDTO_leavesDefaults() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(new HashMap<>()), builder);

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
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.SCAN_PACKAGE, "com.example.app"), builder);

        assertEquals("com.example.app", builder.build().getScanPackage());
    }

    @Test
    void apply_scanPackage_trims() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.SCAN_PACKAGE, "  com.example.app  "), builder);

        assertEquals("com.example.app", builder.build().getScanPackage());
    }

    @Test
    void apply_scanRecursive() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.SCAN_RECURSIVE, "false"), builder);

        assertFalse(builder.build().isScanRecursive());
    }

    @Test
    void apply_defaultScope() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.SCOPE_DEFAULT, "PROTOTYPE"), builder);

        assertEquals(Scope.PROTOTYPE, builder.build().getDefaultScope());
    }

    @Test
    void apply_defaultScope_lowercase() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.SCOPE_DEFAULT, "prototype"), builder);

        assertEquals(Scope.PROTOTYPE, builder.build().getDefaultScope());
    }

    @Test
    void apply_lazy() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.LAZY, "true"), builder);

        assertTrue(builder.build().isLazyInit());
    }

    @Test
    void apply_loggingLevel() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG"), builder);

        assertEquals(LogLevel.DEBUG, builder.build().getLoggingLevel());
    }

    @Test
    void apply_allKeys() {
        Map<String, Object> map = new HashMap<>();
        map.put(SvarogConfigKeys.SCAN_PACKAGE, "com.example.app");
        map.put(SvarogConfigKeys.SCAN_RECURSIVE, "false");
        map.put(SvarogConfigKeys.SCOPE_DEFAULT, "PROTOTYPE");
        map.put(SvarogConfigKeys.LAZY, "true");
        map.put(SvarogConfigKeys.CIRCULAR_DETECTION, "STRICT");
        map.put(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(map), builder);

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
        Map<String, Object> map = new HashMap<>();
        map.put("svarog.unknown", "value");
        map.put("other.key", "value");

        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        applier.apply(dto(map), builder);

        SvarogConfig config = builder.build();
        assertEquals("", config.getScanPackage());
        assertFalse(config.isLazyInit());
    }

    @Test
    void apply_invalidBoolean_throws() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        assertThrows(
                SvarogConfigException.class,
                () -> applier.apply(dto(SvarogConfigKeys.LAZY, "yes"), builder));
    }

    @Test
    void apply_invalidEnum_throws() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        assertThrows(
                SvarogConfigException.class,
                () -> applier.apply(dto(SvarogConfigKeys.SCOPE_DEFAULT, "FOO"), builder));
    }

    @Test
    void apply_doesNotOverrideMissingKeys() {
        SvarogConfigBuilder builder = new SvarogConfigBuilder()
                .scanPackage("initial")
                .lazyInit(true);

        applier.apply(dto(SvarogConfigKeys.LOGGING_LEVEL, "DEBUG"), builder);

        SvarogConfig config = builder.build();
        assertEquals("initial", config.getScanPackage());
        assertTrue(config.isLazyInit());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }
}