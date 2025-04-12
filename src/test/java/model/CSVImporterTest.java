package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CSVImporter class.
 */
public class CSVImporterTest {

    private CSVImporter importer;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        importer = new CSVImporter();
    }

    @Test
    public void testImportValidCSV() throws IOException {
        // Create a temporary CSV file
        File tempFile = tempDir.resolve("test_import.csv").toFile();
        String csvContent = "Name,Platform,Category,FollowerCount,Country,AdRate\n" +
                "John Smith,Instagram,Fitness,500000,USA,2500.0\n" +
                "Emma Johnson,YouTube,Beauty,2000000,UK,5000.0";

        Files.writeString(tempFile.toPath(), csvContent);

        // Test importing
        List<Influencer> importedData = importer.importData(tempFile.getAbsolutePath());

        // Verify data
        assertEquals(2, importedData.size());

        Influencer influencer1 = importedData.get(0);
        assertEquals("John Smith", influencer1.getName());
        assertEquals("Instagram", influencer1.getPlatform());
        assertEquals("Fitness", influencer1.getCategory());
        assertEquals(500000, influencer1.getFollowerCount());
        assertEquals("USA", influencer1.getCountry());
        assertEquals(2500.0, influencer1.getAdRate(), 0.001);

        Influencer influencer2 = importedData.get(1);
        assertEquals("Emma Johnson", influencer2.getName());
        assertEquals("YouTube", influencer2.getPlatform());
        assertEquals("Beauty", influencer2.getCategory());
        assertEquals(2000000, influencer2.getFollowerCount());
        assertEquals("UK", influencer2.getCountry());
        assertEquals(5000.0, influencer2.getAdRate(), 0.001);
    }

    @Test
    public void testImportCSVWithQuotedFields() throws IOException {
        // Create a temporary CSV file with quoted fields
        File tempFile = tempDir.resolve("test_quoted.csv").toFile();
        String csvContent = "Name,Platform,Category,FollowerCount,Country,AdRate\n" +
                "\"Smith, John\",Instagram,\"Fitness, Health\",500000,USA,2500.0";

        Files.writeString(tempFile.toPath(), csvContent);

        // Test importing
        List<Influencer> importedData = importer.importData(tempFile.getAbsolutePath());

        // Verify data
        assertEquals(1, importedData.size());

        Influencer influencer = importedData.get(0);
        assertEquals("Smith, John", influencer.getName());
        assertEquals("Instagram", influencer.getPlatform());
        assertEquals("Fitness, Health", influencer.getCategory());
        assertEquals(500000, influencer.getFollowerCount());
        assertEquals("USA", influencer.getCountry());
        assertEquals(2500.0, influencer.getAdRate(), 0.001);
    }

    @Test
    public void testImportInvalidFile() {
        // Test with non-existent file
        List<Influencer> importedData = importer.importData("non_existent_file.csv");
        assertTrue(importedData.isEmpty());

        // Test with null path
        importedData = importer.importData(null);
        assertTrue(importedData.isEmpty());

        // Test with empty path
        importedData = importer.importData("");
        assertTrue(importedData.isEmpty());
    }

    @Test
    public void testImportInvalidCSVData() throws IOException {
        // Create a temporary CSV file with invalid data
        File tempFile = tempDir.resolve("test_invalid.csv").toFile();
        String csvContent = "Name,Platform,Category,FollowerCount,Country,AdRate\n" +
                "John Smith,Instagram,Fitness,invalid_number,USA,2500.0";

        Files.writeString(tempFile.toPath(), csvContent);

        // Test importing
        List<Influencer> importedData = importer.importData(tempFile.getAbsolutePath());

        // Should ignore the invalid line
        assertTrue(importedData.isEmpty());
    }
}