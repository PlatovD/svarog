package io.github.PlatovD.svarog.config.aggregator;

import io.github.PlatovD.svarog.config.LogLevel;
import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.SvarogConfigBuilder;
import io.github.PlatovD.svarog.config.SvarogConfigKeys;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import io.github.PlatovD.svarog.util.config.ClasspathPropertiesReader;
import io.github.PlatovD.svarog.util.config.PropertiesReader;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesConfigAggregatorTest {

    private static final String TEST_FILE = "svarog-aggregator-test.properties";
    private static final String MISSING_FILE = "svarog-aggregator-missing.properties";

    @Test
    void constructor_path_usesClasspathReader() {
        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(TEST_FILE);
        SvarogConfig config = aggregator.load();

        assertEquals("com.example.aggregator", config.getScanPackage());
        assertTrue(config.isLazyInit());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }

    @Test
    void constructor_reader_usesGivenReader() {
        PropertiesReader reader = new ClasspathPropertiesReader(TEST_FILE);
        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(reader);
        SvarogConfig config = aggregator.load();

        assertEquals("com.example.aggregator", config.getScanPackage());
    }

    @Test
    void constructor_nullReader_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new PropertiesConfigAggregator((PropertiesReader) null));
    }

    @Test
    void applyTo_fillsBuilder() {
        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(TEST_FILE);
        SvarogConfigBuilder builder = new SvarogConfigBuilder();
        aggregator.applyTo(builder);

        SvarogConfig config = builder.build();
        assertEquals("com.example.aggregator", config.getScanPackage());
        assertTrue(config.isLazyInit());
        assertEquals(LogLevel.DEBUG, config.getLoggingLevel());
    }

    @Test
    void load_missingFile_returnsDefaults() {
        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(MISSING_FILE);
        SvarogConfig config = aggregator.load();

        assertEquals("", config.getScanPackage());
        assertFalse(config.isLazyInit());
        assertEquals(LogLevel.INFO, config.getLoggingLevel());
    }

    @Test
    void applyTo_customReader_lambda() {
        PropertiesReader customReader = () -> {
            Properties props = new Properties();
            props.setProperty(SvarogConfigKeys.SCAN_PACKAGE, "custom.package");
            return props;
        };

        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(customReader);
        SvarogConfig config = aggregator.load();

        assertEquals("custom.package", config.getScanPackage());
    }

    @Test
    void applyTo_customReader_overridesDefaults() {
        PropertiesReader emptyReader = Properties::new;

        PropertiesConfigAggregator aggregator = new PropertiesConfigAggregator(emptyReader);
        SvarogConfig config = aggregator.load();

        assertEquals("", config.getScanPackage());
        assertTrue(config.isScanRecursive());
        assertEquals(LogLevel.INFO, config.getLoggingLevel());
    }
}