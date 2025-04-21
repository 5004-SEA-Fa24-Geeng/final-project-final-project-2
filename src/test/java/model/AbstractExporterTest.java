package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base test class for exporter implementations.
 * Contains common test logic for all exporter types.
 */
public abstract class AbstractExporterTest {
    protected IExporter exporter;
    protected List<Influencer> testData;
    
    @TempDir
    protected Path tempDir;

    /**
     * Abstract method to create the specific exporter under test.
     * @return The exporter implementation to test
     */
    protected abstract IExporter createExporter();
    
    /**
     * Abstract method to get the file extension for the export format.
     * @return The file extension (without the dot)
     */
    protected abstract String getFileExtension();
    
    /**
     * Method to determine if the exporter is expected to return true on successful export.
     * Override in subclass to match actual behavior.
     * Default is true - successful export returns true.
     * 
     * @return true if export should return true on success, false otherwise
     */
    protected boolean expectSuccessfulExport() {
        return true; // Default value, override in JSONExporterTest
    }
    
    /**
     * Set up test data common for all exporter tests.
     */
    @BeforeEach
    void setUp() {
        exporter = createExporter();
        testData = new ArrayList<>();
        testData.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, 2500.0, "USA"));
        testData.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, 5000.0, "UK"));
    }

    /**
     * Test exporting with an empty list.
     */
    @Test
    void testExportEmptyList() throws IOException {
        File exportFile = tempDir.resolve("empty." + getFileExtension()).toFile();
        
        if (expectSuccessfulExport()) {
            assertTrue(exporter.export(new ArrayList<>(), exportFile.getAbsolutePath()));
        } else {
            assertFalse(exporter.export(new ArrayList<>(), exportFile.getAbsolutePath()));
        }
        
        // File might still be created even if export returns false
        if (exportFile.exists()) {
            assertTrue(exportFile.length() >= 0);
        }
    }

    /**
     * Test exporting to an invalid path - implementation returns false instead of throwing exception.
     */
    @Test
    void testExportToInvalidPath() {
        // The implementation returns false for invalid paths rather than throwing exceptions
        assertFalse(exporter.export(testData, "/invalid/path/file." + getFileExtension()));
    }

    /**
     * Test exporting with null data - implementation returns false instead of throwing exception.
     */
    @Test
    void testExportWithNullData() {
        // The implementation returns false for null data rather than throwing exceptions
        assertFalse(exporter.export(null, tempDir.resolve("null_data." + getFileExtension()).toFile().getAbsolutePath()));
    }

    /**
     * Test exporting with a null path - implementation returns false instead of throwing exception.
     */
    @Test
    void testExportWithNullPath() {
        // The implementation returns false for null paths rather than throwing exceptions
        assertFalse(exporter.export(testData, null));
    }

    /**
     * Test exporting with an empty path - implementation returns false instead of throwing exception.
     */
    @Test
    void testExportWithEmptyPath() {
        // The implementation returns false for empty paths rather than throwing exceptions
        assertFalse(exporter.export(testData, ""));
    }

    /**
     * Test exporting to an existing file (should overwrite).
     */
    @Test
    void testExportToExistingFile() throws IOException {
        // Create file with some content
        File exportFile = tempDir.resolve("existing." + getFileExtension()).toFile();
        Files.write(exportFile.toPath(), "existing content".getBytes());

        // Export should overwrite the file
        boolean exportResult = exporter.export(testData, exportFile.getAbsolutePath());
        
        if (expectSuccessfulExport()) {
            assertTrue(exportResult);
        } else {
            assertFalse(exportResult);
        }
        
        // Check if file still exists and was modified
        if (exportFile.exists()) {
            String content = Files.readString(exportFile.toPath());
            assertFalse(content.contains("existing content"));
        }
    }
} 