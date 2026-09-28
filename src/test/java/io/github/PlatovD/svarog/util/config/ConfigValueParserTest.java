package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigValueParserTest {

    @Test
    void parseBoolean_true() {
        assertTrue(ConfigValueParser.parseBoolean("true", "key"));
    }

    @Test
    void parseBoolean_uppercase() {
        assertTrue(ConfigValueParser.parseBoolean("TRUE", "key"));
    }

    @Test
    void parseBoolean_mixedCase() {
        assertTrue(ConfigValueParser.parseBoolean("TrUe", "key"));
    }

    @Test
    void parseBoolean_withSpaces() {
        assertTrue(ConfigValueParser.parseBoolean("  true  ", "key"));
    }

    @Test
    void parseBoolean_false() {
        assertFalse(ConfigValueParser.parseBoolean("false", "key"));
    }

    @Test
    void parseBoolean_invalid_throws() {
        SvarogConfigException ex = assertThrows(
                SvarogConfigException.class,
                () -> ConfigValueParser.parseBoolean("yes", "key"));
        assertTrue(ex.getMessage().contains("yes"));
        assertTrue(ex.getMessage().contains("key"));
    }

    @Test
    void parseBoolean_empty_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> ConfigValueParser.parseBoolean("", "key"));
    }

    @Test
    void parseEnum_exact() {
        assertEquals(Scope.SINGLETON,
                ConfigValueParser.parseEnum("SINGLETON", Scope.class, "key"));
    }

    @Test
    void parseEnum_lowercase() {
        assertEquals(Scope.SINGLETON,
                ConfigValueParser.parseEnum("singleton", Scope.class, "key"));
    }

    @Test
    void parseEnum_withSpaces() {
        assertEquals(Scope.PROTOTYPE,
                ConfigValueParser.parseEnum("  prototype  ", Scope.class, "key"));
    }

    @Test
    void parseEnum_invalid_throws() {
        SvarogConfigException ex = assertThrows(
                SvarogConfigException.class,
                () -> ConfigValueParser.parseEnum("FOO", Scope.class, "key"));
        assertTrue(ex.getMessage().contains("FOO"));
        assertTrue(ex.getMessage().contains("SINGLETON"));
        assertTrue(ex.getMessage().contains("PROTOTYPE"));
    }
}