package io.github.PlatovD.svarog.config.reader;

import io.github.PlatovD.svarog.config.dto.ConfigDTO;
import io.github.PlatovD.svarog.exception.SvarogConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesReaderTest {

    private static final String TEST_FILE = "svarog-reader-test.properties";
    private static final String EMPTY_FILE = "svarog-reader-empty.properties";
    private static final String MISSING_FILE = "svarog-reader-missing.properties";

    @Test
    void read_existingFile_returnsKeys() {
        PropertiesReader reader = new PropertiesReader(TEST_FILE);
        ConfigDTO dto = reader.read();

        assertEquals("com.example.test", dto.getString("svarog.scan.package"));
        assertEquals("true", dto.getString("svarog.lazy"));
        assertEquals("DEBUG", dto.getString("svarog.logging.level"));
    }

    @Test
    void read_existingFile_hasKey() {
        PropertiesReader reader = new PropertiesReader(TEST_FILE);
        ConfigDTO dto = reader.read();

        assertTrue(dto.has("svarog.scan.package"));
        assertTrue(dto.has("svarog.lazy"));
        assertFalse(dto.has("svarog.unknown"));
    }

    @Test
    void read_emptyFile_returnsEmptyDTO() {
        PropertiesReader reader = new PropertiesReader(EMPTY_FILE);
        ConfigDTO dto = reader.read();

        assertFalse(dto.has("svarog.scan.package"));
        assertFalse(dto.has("svarog.lazy"));
    }

    @Test
    void read_missingFile_returnsEmptyDTO() {
        PropertiesReader reader = new PropertiesReader(MISSING_FILE);
        ConfigDTO dto = reader.read();

        assertFalse(dto.has("svarog.scan.package"));
        assertFalse(dto.has("svarog.lazy"));
    }

    @Test
    void constructor_nullPath_throws() {
        assertThrows(
                SvarogConfigException.class,
                () -> new PropertiesReader(null));
    }
}