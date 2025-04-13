package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AbstractImporter.
 */
class AbstractImporterTest {

    /**
     * A concrete implementation of AbstractImporter for testing.
     */
    private static class TestImporter extends AbstractImporter {
        @Override
        protected List<Influencer> parseData(String content) {
            // Simple test implementation
            return new ArrayList<>();
        }
    }

    private AbstractImporter importer;

    @BeforeEach
    void setUp() {
        importer = new TestImporter();
    }

    @Test
    void testImportFromNullPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            importer.importFromFile(null);
        });
    }

    @Test
    void testImportFromEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            importer.importFromFile("");
        });
    }

    @Test
    void testImportFromNonExistentFile() {
        assertThrows(RuntimeException.class, () -> {
            importer.importFromFile("non_existent_file.txt");
        });
    }

    @Test
    void testImportFromInvalidPath() {
        assertThrows(RuntimeException.class, () -> {
            importer.importFromFile("/invalid/path/to/file.txt");
        });
    }
}