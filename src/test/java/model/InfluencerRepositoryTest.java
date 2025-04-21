package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for the InfluencerRepository class.
 */
public class InfluencerRepositoryTest {
    private InfluencerRepository repository;
    private Influencer testInfluencer;
    private static final String NAME = "Test Influencer";
    private static final String PLATFORM = "Instagram";
    private static final String CATEGORY = "Fitness";
    private static final int FOLLOWER_COUNT = 1000000;
    private static final String COUNTRY = "USA";
    private static final double AD_RATE = 2500.0;

    @BeforeEach
    void setUp() {
        repository = new InfluencerRepository();
        testInfluencer = new Influencer(NAME, PLATFORM, CATEGORY, FOLLOWER_COUNT, AD_RATE, COUNTRY);
    }

    @Test
    void testSave() {
        repository.save(testInfluencer);
        assertTrue(repository.findAll().contains(testInfluencer));
    }

    @Test
    void testDelete() {
        repository.save(testInfluencer);
        assertTrue(repository.findAll().contains(testInfluencer));
        
        repository.delete(testInfluencer);
        assertFalse(repository.findAll().contains(testInfluencer));
    }

    @Test
    void testFindAll() {
        List<Influencer> initialList = repository.findAll();
        assertTrue(initialList.isEmpty());
        
        repository.save(testInfluencer);
        List<Influencer> updatedList = repository.findAll();
        assertEquals(1, updatedList.size());
        assertTrue(updatedList.contains(testInfluencer));
    }

    @Test
    void testSearchByName() {
        repository.save(testInfluencer);
        
        // Test exact match
        List<Influencer> results = repository.searchByName(NAME);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        // Test partial match
        results = repository.searchByName("Test");
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        // Test no match
        results = repository.searchByName("NonExistent");
        assertEquals(0, results.size());
    }

    @Test
    void testFilterByPlatform() {
        repository.save(testInfluencer);
        
        List<Influencer> results = repository.filterByPlatform(PLATFORM);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        results = repository.filterByPlatform("YouTube");
        assertEquals(0, results.size());
    }

    @Test
    void testFilterByCategory() {
        repository.save(testInfluencer);
        
        List<Influencer> results = repository.filterByCategory(CATEGORY);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        results = repository.filterByCategory("Gaming");
        assertEquals(0, results.size());
    }

    @Test
    void testFilterByFollowerRange() {
        repository.save(testInfluencer);
        
        // Test within range
        List<Influencer> results = repository.filterByFollowerRange(500000, 1500000);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        // Test outside range
        results = repository.filterByFollowerRange(2000000, 3000000);
        assertEquals(0, results.size());
    }

    @Test
    void testFilterByCountry() {
        repository.save(testInfluencer);
        
        List<Influencer> results = repository.filterByCountry(COUNTRY);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        results = repository.filterByCountry("UK");
        assertEquals(0, results.size());
    }

    @Test
    void testSortByName() {
        Influencer influencer2 = new Influencer("Another Influencer", "YouTube", "Gaming", 2000000, 5000.0, "UK");
        repository.save(testInfluencer);
        repository.save(influencer2);
        
        List<Influencer> sorted = repository.sortByName();
        assertEquals(2, sorted.size());
        assertEquals(influencer2, sorted.get(0)); // "Another" comes before "Test"
        assertEquals(testInfluencer, sorted.get(1));
    }

    @Test
    void testSortByFollowers() {
        Influencer influencer2 = new Influencer("Another Influencer", "YouTube", "Gaming", 2000000, 5000.0, "UK");
        repository.save(testInfluencer);
        repository.save(influencer2);
        
        List<Influencer> sorted = repository.sortByFollowers();
        assertEquals(2, sorted.size());
        assertEquals(influencer2, sorted.get(0)); // Higher follower count (2000000)
        assertEquals(testInfluencer, sorted.get(1)); // Lower follower count (1000000)
    }

    @Test
    void testSortByAdRate() {
        Influencer influencer2 = new Influencer("Another Influencer", "YouTube", "Gaming", 2000000, 5000.0, "UK");
        repository.save(testInfluencer);
        repository.save(influencer2);
        
        List<Influencer> sorted = repository.sortByAdRate();
        assertEquals(2, sorted.size());
        assertEquals(influencer2, sorted.get(0)); // Higher ad rate (5000.0)
        assertEquals(testInfluencer, sorted.get(1)); // Lower ad rate (2500.0)
    }

    @Test
    void testSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            repository.save(null));
    }

    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            repository.delete(null));
    }

    @Test
    void testSearchWithNull() {
        List<Influencer> results = repository.searchByName(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testFilterWithInvalidFollowerRange() {

        // Negative min value
        List<Influencer> results1 = repository.filterByFollowerRange(-1, 1000000);
        assertNotNull(results1);
        assertTrue(results1.isEmpty());
        
        // Negative max value
        List<Influencer> results2 = repository.filterByFollowerRange(1000000, -1);
        assertNotNull(results2);
        assertTrue(results2.isEmpty());
        
        // Min > max
        List<Influencer> results3 = repository.filterByFollowerRange(2000000, 1000000);
        assertNotNull(results3);
        assertTrue(results3.isEmpty());
    }
} 