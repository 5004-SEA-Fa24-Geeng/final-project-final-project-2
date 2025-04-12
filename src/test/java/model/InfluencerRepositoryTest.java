package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InfluencerRepository class.
 */
public class InfluencerRepositoryTest {
    private InfluencerRepository repository;
    private Influencer influencer1;
    private Influencer influencer2;
    private Influencer influencer3;

    @BeforeEach
    public void setUp() {
        repository = new InfluencerRepository();

        // Create test influencers
        influencer1 = new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0);
        influencer2 = new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0);
        influencer3 = new Influencer("John Doe", "TikTok", "Comedy", 1000000, "Canada", 3000.0);

        // Add to repository
        repository.save(influencer1);
        repository.save(influencer2);
        repository.save(influencer3);
    }

    @Test
    public void testSave() {
        // Test saving a new influencer
        Influencer newInfluencer = new Influencer("Test", "Twitter", "News", 300000, "USA", 1500.0);
        repository.save(newInfluencer);

        List<Influencer> all = repository.findAll();
        assertEquals(4, all.size());
        assertTrue(all.contains(newInfluencer));

        // Test updating an existing influencer
        Influencer updatedInfluencer = new Influencer("John Smith", "Twitter", "News", 600000, "USA", 3000.0);
        repository.save(updatedInfluencer);

        all = repository.findAll();
        assertEquals(4, all.size()); // Still 4 because we updated an existing one

        // Find the updated influencer
        boolean found = false;
        for (Influencer inf : all) {
            if (inf.getName().equals("John Smith")) {
                assertEquals("Twitter", inf.getPlatform());
                assertEquals("News", inf.getCategory());
                assertEquals(600000, inf.getFollowerCount());
                assertEquals(3000.0, inf.getAdRate(), 0.001);
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testDelete() {
        repository.delete(influencer1);

        List<Influencer> all = repository.findAll();
        assertEquals(2, all.size());
        assertFalse(all.contains(influencer1));
        assertTrue(all.contains(influencer2));
        assertTrue(all.contains(influencer3));
    }

    @Test
    public void testFindAll() {
        List<Influencer> all = repository.findAll();
        assertEquals(3, all.size());
        assertTrue(all.contains(influencer1));
        assertTrue(all.contains(influencer2));
        assertTrue(all.contains(influencer3));
    }

    @Test
    public void testSearchByName() {
        // Search for "John" - should find two influencers
        List<Influencer> results = repository.searchByName("John");
        assertEquals(3, results.size());
        assertTrue(results.contains(influencer1)); // John Smith
        assertTrue(results.contains(influencer3)); // John Doe

        // Search for "Emma" - should find one influencer
        results = repository.searchByName("Emma");
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer2)); // Emma Johnson

        // Search for non-existent name
        results = repository.searchByName("XYZ");
        assertEquals(0, results.size());
    }

    @Test
    public void testFilterByPlatform() {
        // Filter by Instagram
        List<Influencer> results = repository.filterByPlatform("Instagram");
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer1));

        // Filter by YouTube
        results = repository.filterByPlatform("YouTube");
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer2));

        // Filter by non-existent platform
        results = repository.filterByPlatform("Snapchat");
        assertEquals(0, results.size());
    }

    @Test
    public void testFilterByCategory() {
        // Filter by Fitness
        List<Influencer> results = repository.filterByCategory("Fitness");
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer1));

        // Filter by non-existent category
        results = repository.filterByCategory("Technology");
        assertEquals(0, results.size());
    }

    @Test
    public void testFilterByFollowerRange() {
        // Filter 400,000 to 600,000
        List<Influencer> results = repository.filterByFollowerRange(400000, 600000);
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer1)); // 500,000 followers

        // Filter 1,000,000 and above
        results = repository.filterByFollowerRange(1000000, 0);
        assertEquals(2, results.size());
        assertTrue(results.contains(influencer2)); // 2,000,000 followers
        assertTrue(results.contains(influencer3)); // 1,000,000 followers

        // Filter below 100,000
        results = repository.filterByFollowerRange(0, 100000);
        assertEquals(0, results.size());
    }

    @Test
    public void testFilterByCountry() {
        // Filter by USA
        List<Influencer> results = repository.filterByCountry("USA");
        assertEquals(1, results.size());
        assertTrue(results.contains(influencer1));

        // Filter by non-existent country
        results = repository.filterByCountry("France");
        assertEquals(0, results.size());
    }

    @Test
    public void testSortByName() {
        List<Influencer> sorted = repository.sortByName();
        assertEquals(3, sorted.size());

        // Check order: Emma Johnson, John Doe, John Smith
        assertEquals("Emma Johnson", sorted.get(0).getName());
        assertEquals("John Doe", sorted.get(1).getName());
        assertEquals("John Smith", sorted.get(2).getName());
    }

    @Test
    public void testSortByFollowers() {
        List<Influencer> sorted = repository.sortByFollowers();
        assertEquals(3, sorted.size());

        // Check order (descending): Emma Johnson (2M), John Doe (1M), John Smith (500K)
        assertEquals("Emma Johnson", sorted.get(0).getName());
        assertEquals("John Doe", sorted.get(1).getName());
        assertEquals("John Smith", sorted.get(2).getName());
    }

    @Test
    public void testSortByAdRate() {
        List<Influencer> sorted = repository.sortByAdRate();
        assertEquals(3, sorted.size());

        // Check order (descending): Emma Johnson ($5000), John Doe ($3000), John Smith ($2500)
        assertEquals("Emma Johnson", sorted.get(0).getName());
        assertEquals("John Doe", sorted.get(1).getName());
        assertEquals("John Smith", sorted.get(2).getName());
    }
}