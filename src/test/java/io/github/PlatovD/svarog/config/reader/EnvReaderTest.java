package io.github.PlatovD.svarog.config.reader;

import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvReaderTest {

    @Test
    void read_singleVariable_mapsToKey() {
        EnvReader reader = new EnvReader(
                Map.of("SVAROG_SCAN_PACKAGE", "com.example.app"));
        ConfigDTO dto = reader.read();

        assertEquals("com.example.app", dto.getString("svarog.scan.package"));
    }

    @Test
    void read_multipleVariables() {
        EnvReader reader = new EnvReader(Map.of(
                "SVAROG_SCAN_PACKAGE", "com.example.app",
                "SVAROG_LAZY", "true",
                "SVAROG_LOGGING_LEVEL", "DEBUG"
        ));
        ConfigDTO dto = reader.read();

        assertEquals("com.example.app", dto.getString("svarog.scan.package"));
        assertEquals("true", dto.getString("svarog.lazy"));
        assertEquals("DEBUG", dto.getString("svarog.logging.level"));
    }

    @Test
    void read_variableWithoutPrefix_ignored() {
        EnvReader reader = new EnvReader(Map.of(
                "PATH", "/usr/bin",
                "HOME", "/root",
                "SVAROG_LAZY", "true"
        ));
        ConfigDTO dto = reader.read();

        assertFalse(dto.has("PATH"));
        assertFalse(dto.has("svarog.path"));
        assertTrue(dto.has("svarog.lazy"));
        assertEquals("true", dto.getString("svarog.lazy"));
    }

    @Test
    void read_underscoresToDots() {
        EnvReader reader = new EnvReader(
                Map.of("SVAROG_SCAN_PACKAGE", "value"));
        ConfigDTO dto = reader.read();

        assertTrue(dto.has("svarog.scan.package"));
    }

    @Test
    void read_uppercaseToLowercase() {
        EnvReader reader = new EnvReader(
                Map.of("SVAROG_SCAN_RECURSIVE", "true"));
        ConfigDTO dto = reader.read();

        assertTrue(dto.has("svarog.scan.recursive"));
    }

    @Test
    void read_customPrefix() {
        EnvReader reader = new EnvReader(
                Map.of("MYAPP_LAZY", "true"),
                "MYAPP_");
        ConfigDTO dto = reader.read();

        assertEquals("true", dto.getString("svarog.lazy"));
    }

    @Test
    void read_emptyMap_returnsEmptyDTO() {
        EnvReader reader = new EnvReader(Map.of());
        ConfigDTO dto = reader.read();

        assertFalse(dto.has("svarog.scan.package"));
        assertFalse(dto.has("svarog.lazy"));
    }

    @Test
    void constructor_nullEnv_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new EnvReader(null));
    }

    @Test
    void constructor_nullPrefix_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new EnvReader(Map.of(), null));
    }
}