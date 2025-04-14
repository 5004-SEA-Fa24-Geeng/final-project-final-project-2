package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Exporter classes.
 */
public class ExporterTest {
    
    private List<Influencer> influencers;
    private CSVExporter csvExporter;
    private JSONExporter jsonExporter;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    public void setUp() {
        influencers = new ArrayList<>();
        influencers.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        influencers.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));
        
        csvExporter = new CSVExporter();
        jsonExporter = new JSONExporter();
    }
    
    @Test
    public void testCSVExport() throws IOException {
        // Create a temporary file
        File tempFile = tempDir.resolve("export.csv").toFile();
        String filePath = tempFile.getAbsolutePath();
        
        // Test export
        boolean result = csvExporter.export(influencers, filePath);
        assertTrue(result);
        assertTrue(tempFile.exists());
        
        // Verify file content
        String content = Files.readString(tempFile.toPath());
        
        // Check for header
        assertTrue(content.contains("Name,Platform,Category,FollowerCount,Country,AdRate"));
        
        // Check for data
        assertTrue(content.contains("John Smith,Instagram,Fitness,500000,USA,2500.0"));
        assertTrue(content.contains("Emma Johnson,YouTube,Beauty,2000000,UK,5000.0"));
    }
    
    @Test
    public void testCSVExportWithEmptyList() throws IOException {
        // Create a temporary file
        File tempFile = tempDir.resolve("export_empty.csv").toFile();
        String filePath = tempFile.getAbsolutePath();
        
        // Test export with empty list
        boolean result = csvExporter.export(new ArrayList<>(), filePath);
        assertTrue(result);
        assertTrue(tempFile.exists());
        
        // Verify file content
        String content = Files.readString(tempFile.toPath());
        
        // Check for header only
        assertEquals("Name,Platform,Category,FollowerCount,Country,AdRate", content.trim());
    }
    
    @Test
    public void testCSVExportWithNullParameters() {
        // Test with null data
        boolean result = csvExporter.export(null, "file.csv");
        assertFalse(result);
        
        // Test with null path
        result = csvExporter.export(influencers, null);
        assertFalse(result);
        
        // Test with empty path
        result = csvExporter.export(influencers, "");
        assertFalse(result);
    }
    
    @Test
    public void testJSONExport() throws IOException {
        // Create a temporary file
        File tempFile = tempDir.resolve("export.json").toFile();
        String filePath = tempFile.getAbsolutePath();
        
        // Test export
        boolean result = jsonExporter.export(influencers, filePath);
        assertTrue(result);
        assertTrue(tempFile.exists());
        
        // Verify file content
        String content = Files.readString(tempFile.toPath());
        
        // Check JSON format
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]"));
        
        // Check for data
        assertTrue(content.contains("\"name\": \"John Smith\""));
        assertTrue(content.contains("\"platform\": \"Instagram\""));
        assertTrue(content.contains("\"category\": \"Fitness\""));
        assertTrue(content.contains("\"followerCount\": 500000"));
        assertTrue(content.contains("\"country\": \"USA\""));
        assertTrue(content.contains("\"adRate\": 2500.0"));
        
        assertTrue(content.contains("\"name\": \"Emma Johnson\""));
        assertTrue(content.contains("\"platform\": \"YouTube\""));
        assertTrue(content.contains("\"category\": \"Beauty\""));
        assertTrue(content.contains("\"followerCount\": 2000000"));
        assertTrue(content.contains("\"country\": \"UK\""));
        assertTrue(content.contains("\"adRate\": 5000.0"));
    }
    
    @Test
    public void testJSONExportWithEmptyList() throws IOException {
        // Create a temporary file
        File tempFile = tempDir.resolve("export_empty.json").toFile();
        String filePath = tempFile.getAbsolutePath();
        
        // Test export with empty list
        boolean result = jsonExporter.export(new ArrayList<>(), filePath);
        assertTrue(result);
        assertTrue(tempFile.exists());
        
        // Verify file content
        String content = Files.readString(tempFile.toPath());
        
        // Check for empty array
        assertEquals("[]", content.trim());
    }
    
    @Test
    public void testJSONExportWithNullParameters() {
        // Test with null data
        boolean result = jsonExporter.export(null, "file.json");
        assertFalse(result);
        
        // Test with null path
        result = jsonExporter.export(influencers, null);
        assertFalse(result);
        
        // Test with empty path
        result = jsonExporter.export(influencers, "");
        assertFalse(result);
    }
    
    @Test
    public void testCSVFormatting() {
        // Create influencer with special characters in fields
        Influencer specialCharInfluencer = new Influencer(
                "Test, with comma", 
                "Platform \"with quotes\"", 
                "Category\nwith newline", 
                100000, 
                "Country", 
                1000.0);
        
        List<Influencer> specialList = new ArrayList<>();
        specialList.add(specialCharInfluencer);
        
        // Get formatted data
        String formatted = ((AbstractExporter) csvExporter).prepareExport(specialList);
        
        // Verify that the special characters are properly escaped
        assertTrue(formatted.contains("\"Test, with comma\""));
        assertTrue(formatted.contains("\"Platform \"\"with quotes\"\"\""));
        assertTrue(formatted.contains("\"Category\nwith newline\""));
    }
    
    @Test
    public void testJSONEscaping() {
        // Create influencer with special characters in fields
        Influencer specialCharInfluencer = new Influencer(
                "Test \"quotes\"", 
                "Platform\\backslash", 
                "Category\nwith newline", 
                100000, 
                "Country\twith tab", 
                1000.0);
        
        List<Influencer> specialList = new ArrayList<>();
        specialList.add(specialCharInfluencer);
        
        // Get formatted data
        String formatted = ((AbstractExporter) jsonExporter).prepareExport(specialList);
        
        // Verify that the special characters are properly escaped
        assertTrue(formatted.contains("\"name\": \"Test \\\"quotes\\\"\""));
        assertTrue(formatted.contains("\"platform\": \"Platform\\\\backslash\""));
        assertTrue(formatted.contains("\"category\": \"Category\\nwith newline\""));
        assertTrue(formatted.contains("\"country\": \"Country\\twith tab\""));
    }
} 