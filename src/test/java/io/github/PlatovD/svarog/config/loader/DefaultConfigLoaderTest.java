package io.github.PlatovD.svarog.config.loader;

import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.config.factory.ConfigFactory;
import io.github.PlatovD.svarog.config.factory.PropertiesFactory;
import io.github.PlatovD.svarog.config.reader.EnvReader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConfigLoaderTest {

    private static final String TEST_FILE = "svarog-reader-test.properties";
    private static final String MISSING_FILE = "svarog-loader-missing.properties";

    @Test
    void load_propertiesFile_appliesSettings() {
        DefaultConfigLoader loader = new DefaultConfigLoader(new PropertiesFactory(TEST_FILE));
        SvarogConfig config = loader.load();

        assertEquals("com.example.test", config.getScanPackage());
        assertTrue(config.isLazyInit());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }

    @Test
    void load_missingPropertiesFile_returnsDefaults() {
        DefaultConfigLoader loader = new DefaultConfigLoader(new PropertiesFactory(MISSING_FILE));
        SvarogConfig config = loader.load();

        assertEquals("", config.getScanPackage());
        assertFalse(config.isLazyInit());
        assertEquals(LogLevel.INFO, config.getLoggingLevel());
    }

    @Test
    void load_envReader_appliesSettings() {
        Map<String, String> env = new HashMap<>();
        env.put("SVAROG_SCAN_PACKAGE", "com.example.env");
        env.put("SVAROG_LAZY", "true");

        ConfigFactory factory = () -> new EnvReader(env);
        DefaultConfigLoader loader = new DefaultConfigLoader(factory);
        SvarogConfig config = loader.load();

        assertEquals("com.example.env", config.getScanPackage());
        assertTrue(config.isLazyInit());
    }

    @Test
    void load_customFactory_appliesSettings() {
        ConfigFactory factory = () -> {
            Map<String, Object> map = new HashMap<>();
            map.put(SvarogConfigKeys.SCAN_PACKAGE, "custom.package");
            map.put(SvarogConfigKeys.LOGGING_LEVEL, "WARN");
            ConfigDTO dto = new ConfigDTO(map);
            return () -> dto;
        };

        DefaultConfigLoader loader = new DefaultConfigLoader(factory);
        SvarogConfig config = loader.load();

        assertEquals("custom.package", config.getScanPackage());
        assertEquals(LogLevel.WARN, config.getLoggingLevel());
    }

    @Test
    void applyTo_fillsBuilder() {
        DefaultConfigLoader loader = new DefaultConfigLoader(new PropertiesFactory(TEST_FILE));
        io.github.PlatovD.svarog.config.SvarogConfigBuilder builder =
                new io.github.PlatovD.svarog.config.SvarogConfigBuilder();
        loader.applyTo(builder);

        SvarogConfig config = builder.build();
        assertEquals("com.example.test", config.getScanPackage());
    }

    @Test
    void constructor_nullFactory_throws() {
        assertThrows(
                NullPointerException.class,
                () -> new DefaultConfigLoader(null));
    }
}