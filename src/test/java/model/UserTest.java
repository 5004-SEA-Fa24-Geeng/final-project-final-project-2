package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 */
public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "password123");
    }

    @Test
    public void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testIsSubscribed() {
        // Default is not subscribed
        assertFalse(user.isSubscribed());
    }

    @Test
    public void testSubscribe() {
        user.subscribe();
        assertTrue(user.isSubscribed());
    }

    @Test
    public void testUnsubscribe() {
        // First subscribe
        user.subscribe();
        assertTrue(user.isSubscribed());

        // Then unsubscribe
        user.unsubscribe();
        assertFalse(user.isSubscribed());
    }

    @Test
    public void testEquals() {
        User sameUser = new User("testuser", "password123");
        User differentUsername = new User("otheruser", "password123");
        User differentPassword = new User("testuser", "otherpassword");

        assertEquals(user, sameUser);
        assertNotEquals(user, differentUsername);
        assertNotEquals(user, differentPassword);

        // Test after subscribing
        user.subscribe();
        assertNotEquals(user, sameUser);

        // Subscribe the other user too
        sameUser.subscribe();
        assertEquals(user, sameUser);
    }
}