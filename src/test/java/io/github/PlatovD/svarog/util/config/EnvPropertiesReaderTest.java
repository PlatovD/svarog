package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EnvPropertiesReaderTest {

    @Test
    void read_singleVariable_mapsToProperty() {
        EnvPropertiesReader reader = new EnvPropertiesReader(
                Map.of("SVAROG_SCAN_PACKAGE", "com.example.app"));
        Properties props = reader.read();

        assertEquals("com.example.app",
                props.getProperty(SvarogConfigKeys.SCAN_PACKAGE));
    }

    @Test
    void read_multipleVariables() {
        EnvPropertiesReader reader = new EnvPropertiesReader(Map.of(
                "SVAROG_SCAN_PACKAGE", "com.example.app",
                "SVAROG_LAZY", "true",
                "SVAROG_LOGGING_LEVEL", "DEBUG"
        ));
        Properties props = reader.read();

        assertEquals("com.example.app",
                props.getProperty(SvarogConfigKeys.SCAN_PACKAGE));
        assertEquals("true",
                props.getProperty(SvarogConfigKeys.LAZY));
        assertEquals("DEBUG",
                props.getProperty(SvarogConfigKeys.LOGGING_LEVEL));
    }

    @Test
    void read_variableWithoutPrefix_ignored() {
        EnvPropertiesReader reader = new EnvPropertiesReader(Map.of(
                "PATH", "/usr/bin",
                "HOME", "/root",
                "SVAROG_LAZY", "true"
        ));
        Properties props = reader.read();

        assertEquals(1, props.size());
        assertEquals("true", props.getProperty(SvarogConfigKeys.LAZY));
    }

    @Test
    void read_underscoresToDots() {
        EnvPropertiesReader reader = new EnvPropertiesReader(
                Map.of("SVAROG_SCAN_PACKAGE", "value"));
        Properties props = reader.read();

        assertTrue(props.containsKey(SvarogConfigKeys.SCAN_PACKAGE));
    }

    @Test
    void read_uppercaseToLowercase() {
        EnvPropertiesReader reader = new EnvPropertiesReader(
                Map.of("SVAROG_SCAN_RECURSIVE", "true"));
        Properties props = reader.read();

        assertTrue(props.containsKey(SvarogConfigKeys.SCAN_RECURSIVE));
    }

    @Test
    void read_customPrefix() {
        EnvPropertiesReader reader = new EnvPropertiesReader(
                Map.of("MYAPP_LAZY", "true"),
                "MYAPP_");
        Properties props = reader.read();

        assertEquals("true", props.getProperty(SvarogConfigKeys.LAZY));
    }

    @Test
    void read_emptyMap_returnsEmptyProperties() {
        EnvPropertiesReader reader = new EnvPropertiesReader(Map.of());
        Properties props = reader.read();

        assertNotNull(props);
        assertTrue(props.isEmpty());
    }

    @Test
    void constructor_nullEnv_throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnvPropertiesReader(null));
    }

    @Test
    void constructor_nullPrefix_throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnvPropertiesReader(Map.of(), null));
    }
}