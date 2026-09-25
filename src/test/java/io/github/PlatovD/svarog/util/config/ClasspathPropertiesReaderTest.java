package io.github.PlatovD.svarog.util.config;

import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ClasspathPropertiesReaderTest {

    private static final String TEST_FILE = "svarog-test.properties";
    private static final String EMPTY_FILE = "svarog-empty.properties";
    private static final String MISSING_FILE = "svarog-missing.properties";

    @Test
    void read_existingFile_returnsProperties() {
        ClasspathPropertiesReader reader = new ClasspathPropertiesReader(TEST_FILE);
        Properties props = reader.read();

        assertEquals("com.example.test", props.getProperty("svarog.scan.package"));
        assertEquals("true", props.getProperty("svarog.lazy"));
        assertEquals("DEBUG", props.getProperty("svarog.logging.level"));
    }

    @Test
    void read_existingFile_notNull() {
        ClasspathPropertiesReader reader = new ClasspathPropertiesReader(TEST_FILE);
        assertNotNull(reader.read());
    }

    @Test
    void read_emptyFile_returnsEmptyProperties() {
        ClasspathPropertiesReader reader = new ClasspathPropertiesReader(EMPTY_FILE);
        Properties props = reader.read();

        assertNotNull(props);
        assertTrue(props.isEmpty());
    }

    @Test
    void read_missingFile_returnsEmptyProperties() {
        ClasspathPropertiesReader reader = new ClasspathPropertiesReader(MISSING_FILE);
        Properties props = reader.read();

        assertNotNull(props);
        assertTrue(props.isEmpty());
    }

    @Test
    void constructor_nullPath_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new ClasspathPropertiesReader(null));
    }
}