package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for the CSVImporter class.
 */
public class CSVImporterTest {
    
    private CSVImporter importer;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        importer = new CSVImporter();
    }
    
    @Test
    void testValidCSVImport() throws IOException {
        // Create a temporary CSV file with valid data
        File csvFile = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,platform,category,followers,country,adRate\n");
            writer.write("John Smith,Instagram,Fitness,500000,USA,2500.0\n");
            writer.write("Emma Johnson,YouTube,Beauty,2000000,UK,5000.0\n");
        }

        List<Influencer> influencers = importer.importData(csvFile.getAbsolutePath());
        assertEquals(2, influencers.size());

        Influencer influencer1 = influencers.get(0);
        assertEquals("John Smith", influencer1.getName());
        assertEquals("Instagram", influencer1.getPlatform());
        assertEquals("Fitness", influencer1.getCategory());
        assertEquals(500000, influencer1.getFollowers());
        assertEquals("USA", influencer1.getCountry());
        assertEquals(2500.0, influencer1.getAdRate());

        Influencer influencer2 = influencers.get(1);
        assertEquals("Emma Johnson", influencer2.getName());
        assertEquals("YouTube", influencer2.getPlatform());
        assertEquals("Beauty", influencer2.getCategory());
        assertEquals(2000000, influencer2.getFollowers());
        assertEquals("UK", influencer2.getCountry());
        assertEquals(5000.0, influencer2.getAdRate());
    }
    
    @Test
    void testInvalidCSVFormat() throws IOException {
        // Create a temporary CSV file with invalid format
        File csvFile = tempDir.resolve("invalid.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("invalid,header,format\n");
            writer.write("some,invalid,data\n");
        }

        // invalid format yields empty list rather than exception
        List<Influencer> result = importer.importData(csvFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testMissingRequiredFields() throws IOException {
        // Create a temporary CSV file with missing fields
        File csvFile = tempDir.resolve("missing.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,platform,category,followers,country,adRate\n");
            writer.write("John Smith,Instagram,Fitness,,USA,2500.0\n"); // Missing followers
        }

        // missing required numeric field yields empty list
        List<Influencer> result = importer.importData(csvFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testInvalidNumericValues() throws IOException {
        // Create a temporary CSV file with invalid numeric values
        File csvFile = tempDir.resolve("invalid_numbers.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,platform,category,followers,country,adRate\n");
            writer.write("John Smith,Instagram,Fitness,invalid,USA,2500.0\n"); // Invalid followers
        }

        // invalid numeric field yields empty list
        List<Influencer> result = importer.importData(csvFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testEmptyFile() throws IOException {
        // Create an empty CSV file
        File csvFile = tempDir.resolve("empty.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("");
        }

        // empty file yields empty list
        List<Influencer> result = importer.importData(csvFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testHeaderOnlyFile() throws IOException {
        // Create a CSV file with only headers
        File csvFile = tempDir.resolve("header_only.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,platform,category,followers,country,adRate\n");
        }

        List<Influencer> influencers = importer.importData(csvFile.getAbsolutePath());
        assertTrue(influencers.isEmpty());
    }
    
    @Test
    void testNonExistentFile() {
        // non-existent file path returns null (I/O error)
        List<Influencer> result = importer.importData("non_existent.csv");
        assertNull(result);
    }
    
    @Test
    void testNullFilePath() {
        // null file path throws NullPointerException
        assertThrows(NullPointerException.class, () -> importer.importData(null));
    }
    
    @Test
    void testEmptyFilePath() {
        // empty file path returns null (I/O error)
        List<Influencer> result = importer.importData("");
        assertNull(result);
    }
} 