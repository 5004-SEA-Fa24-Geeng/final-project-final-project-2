package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for JSONExporter implementation.
 * Extends the AbstractExporterTest to leverage common test logic.
 * Note: Current implementation appears to always return false when exporting.
 */
public class JSONExporterTest extends AbstractExporterTest {
    
    @Override
    protected IExporter createExporter() {
        return new JSONExporter();
    }
    
    @Override
    protected String getFileExtension() {
        return "json";
    }
    
    /**
     * Override to indicate that JSON exporter returns true on successful export.
     */
    @Override
    protected boolean expectSuccessfulExport() {
        return true; // JSON exporter implementation returns true
    }
    
    /**
     * Test for basic JSON export functionality.
     * Note: Current implementation appears to return false but may still write the file.
     */
    @Test
    void testValidExport() throws IOException {
        File jsonFile = tempDir.resolve("export.json").toFile();
        
        // Export should succeed
        assertTrue(exporter.export(testData, jsonFile.getAbsolutePath()));
        assertTrue(jsonFile.exists());
        
        // Verify file content matches actual JSON format
        String content = Files.readString(jsonFile.toPath());
        assertTrue(content.contains("\"name\": \"John Smith\""));
        assertTrue(content.contains("\"platform\": \"Instagram\""));
        assertTrue(content.contains("\"category\": \"Fitness\""));
        assertTrue(content.contains("\"followers\": 500000"));
        assertTrue(content.contains("\"adRate\": 2500.0"));
        assertTrue(content.contains("\"country\": \"USA\""));
        assertTrue(content.contains("\"name\": \"Emma Johnson\""));
        assertTrue(content.contains("\"platform\": \"YouTube\""));
        assertTrue(content.contains("\"category\": \"Beauty\""));
        assertTrue(content.contains("\"followers\": 2000000"));
        assertTrue(content.contains("\"adRate\": 5000.0"));
        assertTrue(content.contains("\"country\": \"UK\""));
    }
    
    /**
     * Test for JSON-specific empty list export. 
     * Verifies that an empty JSON array is created.
     */
    @Test
    void testJSONSpecificEmptyList() throws IOException {
        File jsonFile = tempDir.resolve("empty_json_specific.json").toFile();
        
        // Export empty list should succeed
        assertTrue(exporter.export(new ArrayList<>(), jsonFile.getAbsolutePath()));
        assertTrue(jsonFile.exists());

        String content = Files.readString(jsonFile.toPath());
        assertEquals("[]", content.trim());
    }
    
    /**
     * Test for special character handling.
     * The current implementation returns false but may still write the file.
     */
    @Test
    void testExportWithSpecialCharacters() throws IOException {
        // Add an influencer with simple special characters that should be handled
        testData.add(new Influencer("John Smith Jr", "Instagram", "Fitness & Health", 500000, 2500.0, "USA"));
        
        File jsonFile = tempDir.resolve("special_chars.json").toFile();
        
        // Export should succeed with special characters
        assertTrue(exporter.export(testData, jsonFile.getAbsolutePath()));
        assertTrue(jsonFile.exists());
        
        String content = Files.readString(jsonFile.toPath());
        assertTrue(content.contains("\"name\": \"John Smith Jr\""));
        assertTrue(content.contains("\"category\": \"Fitness & Health\""));
    }
    
    /**
     * Test for more complex special characters.
     * The current implementation returns false.
     */
    @Test
    void testExportWithMoreComplexSpecialCharacters() throws IOException {
        List<Influencer> complexData = new ArrayList<>();
        // Characters that might be problematic in JSON
        complexData.add(new Influencer("John \"Quote\" Smith", "Instagram", "Fitness\nHealth", 500000, 2500.0, "USA"));
        
        File jsonFile = tempDir.resolve("complex_chars.json").toFile();
        
        // Export should succeed with complex special characters
        assertTrue(exporter.export(complexData, jsonFile.getAbsolutePath()));
        assertTrue(jsonFile.exists());
        
        String content = Files.readString(jsonFile.toPath());
        assertTrue(content.contains("\"name\": \"John \\\"Quote\\\" Smith\""));
        assertTrue(content.contains("\"category\": \"Fitness\\nHealth\""));
    }
    
    /**
     * Test for JSON formatting and structure.
     * The current implementation returns false but may still write a valid JSON file.
     */
    @Test
    void testJSONFormatting() throws IOException {
        File jsonFile = tempDir.resolve("formatting.json").toFile();
        
        // Export should succeed
        assertTrue(exporter.export(testData, jsonFile.getAbsolutePath()));
        assertTrue(jsonFile.exists());
        
        String content = Files.readString(jsonFile.toPath());
        assertNotNull(content);
        assertFalse(content.isEmpty());
        // Verify JSON structure
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]"));
        assertTrue(content.contains("{"));
        assertTrue(content.contains("}"));
        assertTrue(content.contains("\":"));
        assertTrue(content.contains(","));
        
        // Verify proper JSON nesting
        assertTrue(content.indexOf("{") < content.indexOf("}"));
        assertTrue(content.indexOf("[") < content.indexOf("]"));
    }
} 