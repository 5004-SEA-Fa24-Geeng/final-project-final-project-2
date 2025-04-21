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
 * Test class for JSONImporter.
 */
public class JSONImporterTest {
    
    private JSONImporter importer;
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        importer = new JSONImporter();
    }
    
    @Test
    void testValidJSONImport() throws IOException {
        // Create a temporary JSON file with valid data
        File jsonFile = tempDir.resolve("test.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[\n");
            writer.write("  {\n");
            writer.write("    \"name\": \"John Smith\",\n");
            writer.write("    \"platform\": \"Instagram\",\n");
            writer.write("    \"category\": \"Fitness\",\n");
            writer.write("    \"followerCount\": 500000,\n");
            writer.write("    \"country\": \"USA\",\n");
            writer.write("    \"adRate\": 2500.0\n");
            writer.write("  },\n");
            writer.write("  {\n");
            writer.write("    \"name\": \"Emma Johnson\",\n");
            writer.write("    \"platform\": \"YouTube\",\n");
            writer.write("    \"category\": \"Beauty\",\n");
            writer.write("    \"followerCount\": 2000000,\n");
            writer.write("    \"country\": \"UK\",\n");
            writer.write("    \"adRate\": 5000.0\n");
            writer.write("  }\n");
            writer.write("]");
        }

        List<Influencer> influencers = importer.importData(jsonFile.getAbsolutePath());
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
    void testInvalidJSONFormat() throws IOException {
        // Create a temporary JSON file with invalid format
        File jsonFile = tempDir.resolve("invalid.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("{ invalid json format }");
        }

        // Invalid JSON format should return an empty list
        List<Influencer> result = importer.importData(jsonFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMissingRequiredFields() throws IOException {
        // Create a temporary JSON file with missing fields
        File jsonFile = tempDir.resolve("missing.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[\n");
            writer.write("  {\n");
            writer.write("    \"name\": \"John Smith\",\n");
            writer.write("    \"platform\": \"Instagram\",\n");
            writer.write("    \"category\": \"Fitness\",\n");
            writer.write("    \"country\": \"USA\",\n");
            writer.write("    \"adRate\": 2500.0\n");
            writer.write("  }\n");
            writer.write("]");
        }

        // Missing required fields should return an empty list
        List<Influencer> result = importer.importData(jsonFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testInvalidNumericValues() throws IOException {
        // Create a temporary JSON file with invalid numeric values
        File jsonFile = tempDir.resolve("invalid_numbers.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[\n");
            writer.write("  {\n");
            writer.write("    \"name\": \"John Smith\",\n");
            writer.write("    \"platform\": \"Instagram\",\n");
            writer.write("    \"category\": \"Fitness\",\n");
            writer.write("    \"followerCount\": \"invalid\",\n");
            writer.write("    \"country\": \"USA\",\n");
            writer.write("    \"adRate\": 2500.0\n");
            writer.write("  }\n");
            writer.write("]");
        }

        // Invalid numeric values should return an empty list
        List<Influencer> result = importer.importData(jsonFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyFile() throws IOException {
        // Create an empty JSON file
        File jsonFile = tempDir.resolve("empty.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("");
        }

        // Empty file should return an empty list
        List<Influencer> result = importer.importData(jsonFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyArray() throws IOException {
        // Create a JSON file with empty array
        File jsonFile = tempDir.resolve("empty_array.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[]");
        }

        List<Influencer> influencers = importer.importData(jsonFile.getAbsolutePath());
        assertTrue(influencers.isEmpty());
    }

    @Test
    void testNonExistentFile() {
        // Non-existent file should return null (I/O error)
        List<Influencer> result = importer.importData("non_existent.json");
        assertNull(result);
    }

    @Test
    void testNullFilePath() {
        // Null file path throws NullPointerException
        assertThrows(NullPointerException.class, () -> importer.importData(null));
    }

    @Test
    void testEmptyFilePath() {
        // Empty file path returns null (I/O error)
        List<Influencer> result = importer.importData("");
        assertNull(result);
    }
} 