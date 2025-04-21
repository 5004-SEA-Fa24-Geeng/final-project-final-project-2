package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.io.File;

/**
 * Unit tests for the UserManager class.
 */
public class UserManagerTest {
    private UserManager userManager;
    private String uniqueUsername;
    private static final String PASSWORD = "testPass123";
    private static final String USERS_FILE = "data/users.ser";

    @BeforeEach
    void setUp() {
        // Delete existing users file to start with clean state
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // Ignore exceptions
        }
        
        userManager = new UserManager();
        // Generate a unique username for each test to avoid "Username already exists" errors
        uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Test the most basic functionality - check if null methods work properly
     */
    @Test
    void testBasicMethods() {
        // These simple methods should work regardless of User implementation
        assertNull(userManager.findUser(null));
        assertNull(userManager.authenticateUser(null, PASSWORD));
        assertNull(userManager.authenticateUser(uniqueUsername, null));
        assertNull(userManager.findUser("nonExistentUser"));
    }

    /**
     * Test adding users to the manager directly by populating the internal users list
     */
    @Test
    void testUserAdditionAndRetrieval() throws Exception {
        // Directly access the users list using reflection
        Field usersField = UserManager.class.getDeclaredField("users");
        usersField.setAccessible(true);
        List<User> users = (List<User>)usersField.get(userManager);
        
        // Create a test user
        User testUser = new User(uniqueUsername, PASSWORD);
        
        // Ensure username field is set using reflection
        Field usernameField = User.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(testUser, uniqueUsername);
        
        // Add the user to the list
        users.add(testUser);
        
        // Now test the findUser method
        User foundUser = userManager.findUser(uniqueUsername);
        assertNotNull(foundUser, "User should be found after direct addition");
        assertEquals(uniqueUsername, foundUser.getUsername(), "Username should match");
        
        // Test authentication
        User authenticatedUser = userManager.authenticateUser(uniqueUsername, PASSWORD);
        assertNotNull(authenticatedUser, "User should authenticate with correct password");
        
        // Test wrong password
        authenticatedUser = userManager.authenticateUser(uniqueUsername, "wrongPassword");
        assertNull(authenticatedUser, "User should not authenticate with wrong password");
    }
    
    /**
     * Test subscription functionality
     */
    @Test
    void testUserSubscription() throws Exception {
        // Directly access the users list using reflection
        Field usersField = UserManager.class.getDeclaredField("users");
        usersField.setAccessible(true);
        List<User> users = (List<User>)usersField.get(userManager);
        
        // Create a test user
        User testUser = new User(uniqueUsername, PASSWORD);
        
        // Ensure username field is set using reflection
        Field usernameField = User.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(testUser, uniqueUsername);
        
        // Add the user to the list
        users.add(testUser);
        
        // Get the user
        User foundUser = userManager.findUser(uniqueUsername);
        assertNotNull(foundUser);
        
        // Test subscription
        assertFalse(foundUser.isSubscribed(), "User should not be subscribed by default");
        foundUser.subscribe();
        assertTrue(foundUser.isSubscribed(), "User should be subscribed after subscribe() call");
        foundUser.unsubscribe();
        assertFalse(foundUser.isSubscribed(), "User should not be subscribed after unsubscribe() call");
    }
    
    /**
     * Test multiple users functionality
     */
    @Test
    void testMultipleUsers() throws Exception {
        // Directly access the users list using reflection
        Field usersField = UserManager.class.getDeclaredField("users");
        usersField.setAccessible(true);
        List<User> users = (List<User>)usersField.get(userManager);
        
        // Create test users
        String username2 = "user_" + UUID.randomUUID().toString().substring(0, 8);
        User user1 = new User(uniqueUsername, PASSWORD);
        User user2 = new User(username2, "different_password");
        
        // Ensure username fields are set using reflection
        Field usernameField = User.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user1, uniqueUsername);
        usernameField.set(user2, username2);
        
        // Add the users to the list
        users.add(user1);
        users.add(user2);
        
        // Test findUser for both users
        User foundUser1 = userManager.findUser(uniqueUsername);
        User foundUser2 = userManager.findUser(username2);
        
        assertNotNull(foundUser1, "First user should be found");
        assertNotNull(foundUser2, "Second user should be found");
        assertEquals(uniqueUsername, foundUser1.getUsername(), "First username should match");
        assertEquals(username2, foundUser2.getUsername(), "Second username should match");
    }
} 