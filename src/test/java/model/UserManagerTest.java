package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserManager class.
 */
public class UserManagerTest {
    private UserManager userManager;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
        user1 = new User("user1", "password1");
        user2 = new User("user2", "password2");
    }

    @Test
    public void testRegisterUser() {
        // Register users
        userManager.registerUser(user1);
        userManager.registerUser(user2);

        // Verify they can be found
        User foundUser1 = userManager.findUser("user1");
        User foundUser2 = userManager.findUser("user2");

        assertNotNull(foundUser1);
        assertNotNull(foundUser2);
        assertEquals(user1, foundUser1);
        assertEquals(user2, foundUser2);
    }

    @Test
    public void testRegisterDuplicateUser() {
        // Register a user
        userManager.registerUser(user1);

        // Try to register another user with the same username
        User duplicateUser = new User("user1", "differentPassword");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userManager.registerUser(duplicateUser);
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    public void testRegisterNullUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userManager.registerUser(null);
        });

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    public void testAuthenticateUser() {
        // Register users
        userManager.registerUser(user1);
        userManager.registerUser(user2);

        // Correct authentication
        User authenticatedUser = userManager.authenticateUser("user1", "password1");
        assertNotNull(authenticatedUser);
        assertEquals(user1, authenticatedUser);

        // Wrong password
        authenticatedUser = userManager.authenticateUser("user1", "wrongPassword");
        assertNull(authenticatedUser);

        // Wrong username
        authenticatedUser = userManager.authenticateUser("nonExistentUser", "password1");
        assertNull(authenticatedUser);

        // Null parameters
        authenticatedUser = userManager.authenticateUser(null, "password1");
        assertNull(authenticatedUser);

        authenticatedUser = userManager.authenticateUser("user1", null);
        assertNull(authenticatedUser);
    }

    @Test
    public void testFindUser() {
        // Find in empty repository
        User foundUser = userManager.findUser("user1");
        assertNull(foundUser);

        // Register users
        userManager.registerUser(user1);
        userManager.registerUser(user2);

        // Find existing user
        foundUser = userManager.findUser("user1");
        assertNotNull(foundUser);
        assertEquals(user1, foundUser);

        // Find non-existent user
        foundUser = userManager.findUser("nonExistentUser");
        assertNull(foundUser);

        // Find with null parameter
        foundUser = userManager.findUser(null);
        assertNull(foundUser);
    }

    @Test
    public void testGetSubscribedUsers() {
        // Register users
        userManager.registerUser(user1);
        userManager.registerUser(user2);

        // Initially no subscribed users
        List<User> subscribedUsers = userManager.getSubscribedUsers();
        assertTrue(subscribedUsers.isEmpty());

        // Subscribe one user
        user1.subscribe();
        subscribedUsers = userManager.getSubscribedUsers();
        assertEquals(1, subscribedUsers.size());
        assertTrue(subscribedUsers.contains(user1));

        // Subscribe another user
        user2.subscribe();
        subscribedUsers = userManager.getSubscribedUsers();
        assertEquals(2, subscribedUsers.size());
        assertTrue(subscribedUsers.contains(user1));
        assertTrue(subscribedUsers.contains(user2));

        // Unsubscribe a user
        user1.unsubscribe();
        subscribedUsers = userManager.getSubscribedUsers();
        assertEquals(1, subscribedUsers.size());
        assertFalse(subscribedUsers.contains(user1));
        assertTrue(subscribedUsers.contains(user2));
    }
}