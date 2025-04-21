package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Unit tests for the UserFavorites class.
 */
public class UserFavoritesTest {
    private UserFavorites favorites;
    private Influencer testInfluencer;
    private User testUser;
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String NAME = "Test Influencer";
    private static final String PLATFORM = "Instagram";
    private static final String CATEGORY = "Fitness";
    private static final int FOLLOWERS = 1000000;
    private static final String COUNTRY = "USA";
    private static final double AD_RATE = 2500.0;
    private static final String FAVORITES_DIR = "src/main/resources/data/favorites/";

    @BeforeEach
    void setUp() {
        try {
            Files.deleteIfExists(Paths.get(FAVORITES_DIR + USERNAME + ".txt"));
        } catch (IOException e) {
            // Ignore exceptions
        }
        
        testUser = new User(USERNAME, PASSWORD);
        favorites = new UserFavorites(testUser);
        testInfluencer = new Influencer(NAME, PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);
    }

    @Test
    void testAddItem() {
        favorites.addItem(testInfluencer);
        assertTrue(favorites.contains(testInfluencer));
        assertEquals(1, favorites.getAllItems().size());
    }

    @Test
    void testRemoveItem() {
        favorites.addItem(testInfluencer);
        assertTrue(favorites.contains(testInfluencer));
        
        favorites.removeItem(testInfluencer);
        assertFalse(favorites.contains(testInfluencer));
        assertEquals(0, favorites.getAllItems().size());
    }

    @Test
    void testGetAllItems() {
        // Test initial state - favorites should be empty
        List<Influencer> initialItems = favorites.getAllItems();
        assertEquals(0, initialItems.size(), "A newly created favorites list should be empty");
        
        // Test state after adding an item
        favorites.addItem(testInfluencer);
        List<Influencer> allItems = favorites.getAllItems();
        assertEquals(1, allItems.size(), "After adding one item, the list should have 1 item");
        assertTrue(allItems.contains(testInfluencer));
    }

    @Test
    void testContains() {
        // Test initial state - confirm the list is empty
        assertFalse(favorites.contains(testInfluencer), "A newly created favorites list should not contain any items");
        
        // Test after adding an item
        favorites.addItem(testInfluencer);
        assertTrue(favorites.contains(testInfluencer), "After adding an item, it should be contained in the favorites");
    }

    @Test
    void testSearchByName() {
        favorites.addItem(testInfluencer);
        
        // Test exact match
        List<Influencer> results = favorites.searchByName(NAME);
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        // Test partial match
        results = favorites.searchByName("Test");
        assertEquals(1, results.size());
        assertEquals(testInfluencer, results.get(0));
        
        // Test no match
        results = favorites.searchByName("NonExistent");
        assertEquals(0, results.size());
    }

    @Test
    void testAddDuplicate() {
        // Confirm initial state is empty
        assertEquals(0, favorites.getAllItems().size(), "Initial state should be empty");
        
        // Add the same item and check behavior
        favorites.addItem(testInfluencer);
        favorites.addItem(testInfluencer);
        assertEquals(1, favorites.getAllItems().size(), "After adding the same item twice, there should only be 1 item");
    }

    @Test
    void testRemoveNonExistent() {
        // Create an influencer that is not in the favorites
        Influencer nonExistent = new Influencer("Non Existent", "TikTok", "Travel", 500000, 1500.0, "Canada");
        favorites.removeItem(nonExistent);
        assertEquals(0, favorites.getAllItems().size(), "After removing a non-existent item, the list should remain empty");
    }

    @Test
    void testAddNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            favorites.addItem(null));
    }

    @Test
    void testRemoveNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            favorites.removeItem(null));
    }

    @Test
    void testSearchWithNull() {
        // Test passing null to searchByName - should return an empty list rather than throwing an exception
        List<Influencer> results = favorites.searchByName(null);
        assertNotNull(results);
        assertTrue(results.isEmpty(), "Searching with null should return an empty list");
    }

    @Test
    void testSearchWithEmptyString() {
        favorites.addItem(testInfluencer);
        List<Influencer> results = favorites.searchByName("");
        assertEquals(0, results.size());
    }

    @Test
    void testMultipleItems() {
        Influencer influencer2 = new Influencer("Another Influencer", "YouTube", "Gaming", 2000000, AD_RATE, "UK");
        
        favorites.addItem(testInfluencer);
        favorites.addItem(influencer2);
        
        assertEquals(2, favorites.getAllItems().size(), "After adding 2 items, the list should have 2 items");
        assertTrue(favorites.contains(testInfluencer));
        assertTrue(favorites.contains(influencer2));
    }

    @Test
    void testConstructWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> 
            new UserFavorites(null));
    }

    @Test
    void testUsername() {
        assertEquals(USERNAME, favorites.getUsername());
    }
} 