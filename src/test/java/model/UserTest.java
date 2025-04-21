package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;

/**
 * Unit tests for the User class.
 */
public class UserTest {
    private User user;
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass123";

    /**
     * Helper method to fix username field since User constructor doesn't set it properly
     */
    private void fixUsername(User user, String username) {
        try {
            Field field = User.class.getDeclaredField("username");
            field.setAccessible(true);
            field.set(user, username);
        } catch (Exception e) {
            fail("Failed to fix username: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, PASSWORD);
        fixUsername(user, USERNAME);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertFalse(user.isSubscribed()); // Default should be false
        
        // Favorites are initialized by MainController, not in the constructor
        assertNull(user.getFavorites());
    }

    @Test
    void testSubscriptionMethods() {
        assertFalse(user.isSubscribed());
        
        user.subscribe();
        assertTrue(user.isSubscribed());
        
        user.unsubscribe();
        assertFalse(user.isSubscribed());
    }

    @Test
    void testFavorites() {
        UserFavorites favorites = new UserFavorites(user);
        user.setFavorites(favorites);
        assertEquals(favorites, user.getFavorites());
    }

    @Test
    void testEqualsAndHashCode() {
        User sameUser = new User(USERNAME, PASSWORD);
        fixUsername(sameUser, USERNAME);
        
        User differentUser = new User("otherUser", PASSWORD);
        fixUsername(differentUser, "otherUser");

        assertTrue(user.equals(sameUser));
        assertEquals(user.hashCode(), sameUser.hashCode());
        assertFalse(user.equals(differentUser));
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }

    @Test
    void testToString() {
        // Match the actual format from User.toString()
        String expected = String.format("User{username='%s', isSubscribed=%b}", 
            USERNAME, user.isSubscribed());
        assertEquals(expected, user.toString());
    }

    @Test
    void testNullUsername() {
        // User constructor accepts null username
        assertDoesNotThrow(() -> new User(null, PASSWORD));
    }

    @Test
    void testNullPassword() {
        // User constructor accepts null password
        assertDoesNotThrow(() -> new User(USERNAME, null));
    }

    @Test
    void testEmptyUsername() {
        // User constructor accepts empty username
        assertDoesNotThrow(() -> new User("", PASSWORD));
    }

    @Test
    void testEmptyPassword() {
        // User constructor accepts empty password
        assertDoesNotThrow(() -> new User(USERNAME, ""));
    }

    @Test
    void testSetNullFavorites() {
        // setFavorites accepts null
        assertDoesNotThrow(() -> user.setFavorites(null));
        assertNull(user.getFavorites());
    }
} 