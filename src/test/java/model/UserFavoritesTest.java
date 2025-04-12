package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserFavorites class.
 */
public class UserFavoritesTest {
    private User user;
    private UserFavorites favorites;
    private Influencer influencer1;
    private Influencer influencer2;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "password");
        favorites = new UserFavorites(user);

        influencer1 = new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0);
        influencer2 = new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0);
    }

    @Test
    public void testAddItem() {
        // Initially empty
        assertTrue(favorites.getAllItems().isEmpty());

        // Add one item
        favorites.addItem(influencer1);
        List<Influencer> items = favorites.getAllItems();
        assertEquals(1, items.size());
        assertTrue(items.contains(influencer1));

        // Add another item
        favorites.addItem(influencer2);
        items = favorites.getAllItems();
        assertEquals(2, items.size());
        assertTrue(items.contains(influencer1));
        assertTrue(items.contains(influencer2));

        // Try to add duplicate (should not add it again)
        favorites.addItem(influencer1);
        items = favorites.getAllItems();
        assertEquals(2, items.size());
    }

    @Test
    public void testRemoveItem() {
        // Add items
        favorites.addItem(influencer1);
        favorites.addItem(influencer2);
        assertEquals(2, favorites.getAllItems().size());

        // Remove one item
        favorites.removeItem(influencer1);
        List<Influencer> items = favorites.getAllItems();
        assertEquals(1, items.size());
        assertFalse(items.contains(influencer1));
        assertTrue(items.contains(influencer2));

        // Remove the other item
        favorites.removeItem(influencer2);
        items = favorites.getAllItems();
        assertTrue(items.isEmpty());

        // Try to remove a non-existent item (should not throw exception)
        favorites.removeItem(influencer1);
    }

    @Test
    public void testGetAllItems() {
        // Initially empty
        assertTrue(favorites.getAllItems().isEmpty());

        // Add items
        favorites.addItem(influencer1);
        favorites.addItem(influencer2);

        // Get all items
        List<Influencer> items = favorites.getAllItems();
        assertEquals(2, items.size());
        assertTrue(items.contains(influencer1));
        assertTrue(items.contains(influencer2));

        // Verify the returned list is a copy (modifying it shouldn't affect the original)
        items.remove(0);
        assertEquals(1, items.size());
        assertEquals(2, favorites.getAllItems().size());
    }

    @Test
    public void testSearchByName() {
        // Add items
        favorites.addItem(influencer1); // John Smith
        favorites.addItem(influencer2); // Emma Johnson
        favorites.addItem(new Influencer("John Doe", "TikTok", "Comedy", 1000000, "Canada", 3000.0));

        // Search for "John" - should find two influencers
        List<Influencer> results = favorites.searchByName("John");
        assertEquals(3, results.size());

        // Search for "Emma" - should find one influencer
        results = favorites.searchByName("Emma");
        assertEquals(1, results.size());
        assertEquals("Emma Johnson", results.get(0).getName());

        // Search for non-existent name
        results = favorites.searchByName("XYZ");
        assertEquals(0, results.size());

        // Search with empty string - should return all
        results = favorites.searchByName("");
        assertEquals(3, results.size());
    }

    @Test
    public void testGetUser() {
        assertEquals(user, favorites.getUser());
    }

    @Test
    public void testIllegalArgument() {
        // Try to add a non-influencer object
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            favorites.addItem("Not an influencer");
        });
        assertTrue(exception.getMessage().contains("must be an Influencer"));

        // Try to remove a non-influencer object
        exception = assertThrows(IllegalArgumentException.class, () -> {
            favorites.removeItem("Not an influencer");
        });
        assertTrue(exception.getMessage().contains("must be an Influencer"));
    }
}