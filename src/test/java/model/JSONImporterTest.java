package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JSONImporter.
 */
class JSONImporterTest {

    private JSONImporter importer;

    @BeforeEach
    void setUp() {
        importer = new JSONImporter();
    }

    @Test
    void testParseValidJSON() {
        String json = """
            {
                "name": "John Smith",
                "platform": "Instagram",
                "category": "Fitness",
                "followerCount": "500000",
                "country": "USA",
                "adRate": "2500.0"
            }
            """;

        List<Influencer> result = importer.parseData(json);

        assertNotNull(result);
        assertEquals(1, result.size());
        Influencer influencer = result.get(0);
        assertEquals("John Smith", influencer.getName());
        assertEquals("Instagram", influencer.getPlatform());
        assertEquals("Fitness", influencer.getCategory());
        assertEquals(500000, influencer.getFollowerCount());
        assertEquals("USA", influencer.getCountry());
        assertEquals(2500.0, influencer.getAdRate());
    }

    @Test
    void testParseMultipleInfluencers() {
        String json = """
            {
                "name": "John Smith",
                "platform": "Instagram",
                "category": "Fitness",
                "followerCount": "500000",
                "country": "USA",
                "adRate": "2500.0"
            }
            {
                "name": "Jane Doe",
                "platform": "YouTube",
                "category": "Beauty",
                "followerCount": "1000000",
                "country": "UK",
                "adRate": "5000.0"
            }
            """;

        List<Influencer> result = importer.parseData(json);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first influencer
        Influencer influencer1 = result.get(0);
        assertEquals("John Smith", influencer1.getName());
        assertEquals("Instagram", influencer1.getPlatform());

        // Verify second influencer
        Influencer influencer2 = result.get(1);
        assertEquals("Jane Doe", influencer2.getName());
        assertEquals("YouTube", influencer2.getPlatform());
    }

    @Test
    void testParseEmptyContent() {
        List<Influencer> result = importer.parseData("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseNullContent() {
        List<Influencer> result = importer.parseData(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseInvalidJSON() {
        String invalidJson = """
            {
                "name": "John Smith",
                "platform": "Instagram",
                "category": "Fitness",
                "followerCount": "invalid",
                "country": "USA",
                "adRate": "2500.0"
            }
            """;

        List<Influencer> result = importer.parseData(invalidJson);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseMissingFields() {
        String incompleteJson = """
            {
                "name": "John Smith",
                "platform": "Instagram"
            }
            """;

        List<Influencer> result = importer.parseData(incompleteJson);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseInvalidNumbers() {
        String invalidNumbersJson = """
            {
                "name": "John Smith",
                "platform": "Instagram",
                "category": "Fitness",
                "followerCount": "abc",
                "country": "USA",
                "adRate": "xyz"
            }
            """;

        List<Influencer> result = importer.parseData(invalidNumbersJson);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 