package view;

import model.Influencer;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.MainController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the MainView class.
 */
public class MainViewTest {
    private MainView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        // Redirect System.out to capturable stream
        System.setOut(new PrintStream(outContent));

        view = new MainView();

        // Create a test user
        User testUser = new User("testUser", "password");
        view.setCurrentUser(testUser);

        // Create test data
        List<Influencer> testInfluencers = new ArrayList<>();
        testInfluencers.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, 2500.0, "USA"));
        testInfluencers.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, 5000.0, "UK"));

        // Set influencers list
        view.displayInfluencers(testInfluencers);

        // Create test favorites
        List<Influencer> testFavorites = new ArrayList<>();
        testFavorites.add(new Influencer("David Lee", "TikTok", "Comedy", 1500000, 3000.0, "Canada"));

        // Set favorites list
        view.displayFavorites(testFavorites);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testGetCurrentInfluencers() {
        // Test getCurrentInfluencers method
        List<Influencer> currentInfluencers = view.getCurrentInfluencers();

        // Verify returned list
        assertEquals(2, currentInfluencers.size());
        assertEquals("John Smith", currentInfluencers.get(0).getName());
        assertEquals("Emma Johnson", currentInfluencers.get(1).getName());

        // Verify we get a copy, not a reference to the original list
        currentInfluencers.add(new Influencer("Test", "Test", "Test", 100, 100.0, "Test"));
        assertEquals(2, view.getCurrentInfluencers().size()); // Original list should be unchanged
    }

    @Test
    public void testGetCurrentFavorites() {
        // Test getCurrentFavorites method
        List<Influencer> currentFavorites = view.getCurrentFavorites();

        // Verify returned list
        assertEquals(1, currentFavorites.size());
        assertEquals("David Lee", currentFavorites.get(0).getName());

        // Verify we get a copy, not a reference to the original list
        currentFavorites.add(new Influencer("Test", "Test", "Test", 100, 100.0, "Test"));
        assertEquals(1, view.getCurrentFavorites().size()); // Original list should be unchanged
    }

    @Test
    public void testViewStateTransitions() {
        // Test view state transitions
        assertEquals(ViewState.LOGIN, view.getCurrentState()); // Default state after initialization

        // Test transition to influencer list view
        view.showInfluencerListView();
        assertEquals(ViewState.INFLUENCER_LIST, view.getCurrentState());

        // Test transition to favorites view
        view.showUserFavoritesView();
        assertEquals(ViewState.USER_FAVORITES, view.getCurrentState());

        // Test transition to export view
        view.showExportView();
        assertEquals(ViewState.EXPORT, view.getCurrentState());

        // Test transition to import view
        view.showImportView();
        assertEquals(ViewState.IMPORT, view.getCurrentState());

        // Test transition back to user profile view
        view.showUserView();
        assertEquals(ViewState.USER_PROFILE, view.getCurrentState());
    }

    @Test
    public void testRenderInfluencerListMenu() {
        // Clear output
        outContent.reset();

        // Display influencer list view
        view.setVisible(true);
        view.showInfluencerListView();

        // Check if output contains the 13 options (including Reset option)
        String output = outContent.toString();
        assertTrue(output.contains("13. Reset to All Influencers"));
    }

    // New tests for improved coverage

    @Test
    public void testShowLoginForm() {
        // Reset output
        outContent.reset();

        // Set view to visible
        view.setVisible(true);

        // Show login form
        view.showLoginForm();

        // Verify state changes
        assertEquals(ViewState.LOGIN, view.getCurrentState());

        // Verify view is rendered correctly
        String output = outContent.toString();
        assertTrue(output.contains("=== Login ==="));
        assertTrue(output.contains("1. Enter Username and Password"));
        assertTrue(output.contains("2. Register a New Account"));
    }

    @Test
    public void testShowRegistrationForm() {
        // Reset output
        outContent.reset();

        // Set view to visible
        view.setVisible(true);

        // Show registration form
        view.showRegistrationForm();

        // Verify state changes
        assertEquals(ViewState.REGISTRATION, view.getCurrentState());

        // Verify view is rendered correctly
        String output = outContent.toString();
        assertTrue(output.contains("=== Register a New Account ==="));
        assertTrue(output.contains("Please enter your details:"));
    }

    @Test
    public void testShowUserProfile() {
        // Reset output
        outContent.reset();

        // Set view to visible
        view.setVisible(true);

        // Create a new test user
        User newUser = new User("newUser", "password123");
        newUser.subscribe(); // Make user a premium subscriber

        // Show user profile with the new user
        view.showUserProfile(newUser);

        // Verify state changes
        assertEquals(ViewState.USER_PROFILE, view.getCurrentState());

        // Verify the current user was updated
        String output = outContent.toString();
        assertTrue(output.contains("Username: newUser"));
        assertTrue(output.contains("Subscription Status: Premium"));
    }

    @Test
    public void testShowError() {
        // Create a scanner with simulated input for nextLine()
        Scanner mockScanner = new Scanner("mock input");
        try {
            // Use reflection to replace the scanner
            java.lang.reflect.Field scannerField = MainView.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner originalScanner = (Scanner) scannerField.get(view);
            scannerField.set(view, mockScanner);

            // Reset output
            outContent.reset();

            // Show an error
            view.showError("Test error message");

            // Verify error message is displayed
            String output = outContent.toString();
            assertTrue(output.contains("[ERROR] Test error message"));
            assertTrue(output.contains("Press Enter to continue..."));

            // Restore original scanner
            scannerField.set(view, originalScanner);
        } catch (Exception e) {
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }

    @Test
    public void testSetController() {
        // Create a mock controller
        MainController mockController = mock(MainController.class);

        // Set the controller
        view.setController(mockController);

        // We can't directly verify the controller was set (it's private)
        // But we verify no exceptions are thrown
        assertDoesNotThrow(() -> view.setController(mockController));
    }

    @Test
    public void testDisplayMessage() {
        // Reset output
        outContent.reset();

        // Display a message
        view.displayMessage("Test message");

        // Verify the message is displayed
        assertEquals("Test message\n", outContent.toString());
    }

    @Test
    public void testShouldShowAdRate() {
        // Test with subscribed user
        User subscribedUser = new User("subscriber", "password");
        subscribedUser.subscribe();
        view.setCurrentUser(subscribedUser);
        assertTrue(view.shouldShowAdRate());

        // Test with non-subscribed user
        User nonSubscribedUser = new User("nonSubscriber", "password");
        view.setCurrentUser(nonSubscribedUser);
        assertFalse(view.shouldShowAdRate());

        // Test with null user
        view.setCurrentUser(null);
        assertFalse(view.shouldShowAdRate());
    }

    @Test
    public void testShowExportOptions() {
        // Reset output
        outContent.reset();

        // Set view to visible
        view.setVisible(true);

        // Show export options
        view.showExportOptions();

        // Verify state changes
        assertEquals(ViewState.EXPORT, view.getCurrentState());

        // Verify correct options are displayed
        String output = outContent.toString();
        assertTrue(output.contains("=== Export Data ==="));
        assertTrue(output.contains("1. Export as CSV"));
        assertTrue(output.contains("2. Export as JSON"));
    }

    @Test
    public void testShowExportSuccess() {
        // Create a scanner with simulated input for nextLine()
        Scanner mockScanner = new Scanner("mock input");
        try {
            // Use reflection to replace the scanner
            java.lang.reflect.Field scannerField = MainView.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner originalScanner = (Scanner) scannerField.get(view);
            scannerField.set(view, mockScanner);

            // Reset output
            outContent.reset();

            // Show export success
            view.showExportSuccess("test_export.csv");

            // Verify success message is displayed
            String output = outContent.toString();
            assertTrue(output.contains("[SUCCESS] Data exported successfully to: test_export.csv"));
            assertTrue(output.contains("Press Enter to continue..."));

            // Restore original scanner
            scannerField.set(view, originalScanner);
        } catch (Exception e) {
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }

    @Test
    public void testShowExportError() {
        // Create a scanner with simulated input for nextLine()
        Scanner mockScanner = new Scanner("mock input");
        try {
            // Use reflection to replace the scanner
            java.lang.reflect.Field scannerField = MainView.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner originalScanner = (Scanner) scannerField.get(view);
            scannerField.set(view, mockScanner);

            // Reset output
            outContent.reset();

            // Show export error
            view.showExportError("File not found");

            // Verify error message is displayed
            String output = outContent.toString();
            assertTrue(output.contains("[ERROR] Export failed: File not found"));
            assertTrue(output.contains("Press Enter to continue..."));

            // Restore original scanner
            scannerField.set(view, originalScanner);
        } catch (Exception e) {
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }

    @Test
    public void testShowImportSuccess() {
        // Create a scanner with simulated input for nextLine()
        Scanner mockScanner = new Scanner("mock input");
        try {
            // Use reflection to replace the scanner
            java.lang.reflect.Field scannerField = MainView.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner originalScanner = (Scanner) scannerField.get(view);
            scannerField.set(view, mockScanner);

            // Reset output
            outContent.reset();

            // Show import success
            view.showImportSuccess("5 influencers imported successfully");

            // Verify success message is displayed
            String output = outContent.toString();
            assertTrue(output.contains("[SUCCESS] 5 influencers imported successfully"));
            assertTrue(output.contains("Press Enter to continue..."));

            // Restore original scanner
            scannerField.set(view, originalScanner);
        } catch (Exception e) {
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        // Reset output
        outContent.reset();

        // Set view to visible
        view.setVisible(true);

        // Create new influencer list
        List<Influencer> newInfluencers = new ArrayList<>();
        newInfluencers.add(new Influencer("Alex Wong", "Twitter", "Technology", 300000, 1500.0, "Singapore"));
        newInfluencers.add(new Influencer("Maria Garcia", "TikTok", "Dance", 1200000, 3500.0, "Spain"));

        // Set current state to INFLUENCER_LIST to test rendering
        view.showInfluencerListView();

        // Clear output
        outContent.reset();

        // Update view with new data
        view.update(newInfluencers);

        // Verify the currentInfluencers list was updated
        List<Influencer> updatedList = view.getCurrentInfluencers();
        assertEquals(2, updatedList.size());
        assertEquals("Alex Wong", updatedList.get(0).getName());
        assertEquals("Maria Garcia", updatedList.get(1).getName());

        // Verify view was rendered with new data
        String output = outContent.toString();
        assertTrue(output.contains("Alex Wong"));
        assertTrue(output.contains("Maria Garcia"));
    }

    @Test
    public void testUpdateWithNonInfluencerData() {
        // Create a list of non-Influencer objects
        List<String> stringList = new ArrayList<>();
        stringList.add("Test1");
        stringList.add("Test2");

        // Store current influencers
        List<Influencer> originalInfluencers = view.getCurrentInfluencers();

        // Update view with non-Influencer data
        view.update(stringList);

        // Verify the currentInfluencers list remains unchanged
        assertEquals(originalInfluencers.size(), view.getCurrentInfluencers().size());
    }

    @Test
    public void testDisplaySearchResults() {
        // Create a scanner with simulated input for nextLine()
        Scanner mockScanner = new Scanner("mock input");
        try {
            // Use reflection to replace the scanner
            java.lang.reflect.Field scannerField = MainView.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            Scanner originalScanner = (Scanner) scannerField.get(view);
            scannerField.set(view, mockScanner);

            // Reset output
            outContent.reset();

            // Create search results
            List<Influencer> searchResults = new ArrayList<>();
            searchResults.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, 2500.0, "USA"));

            // Display search results
            view.displaySearchResults(searchResults);

            // Verify results are displayed
            String output = outContent.toString();
            assertTrue(output.contains("==== Search Results ===="));
            assertTrue(output.contains("John Smith"));
            assertTrue(output.contains("Press Enter to continue..."));

            // Restore original scanner
            scannerField.set(view, originalScanner);
        } catch (Exception e) {
            fail("Failed to mock scanner: " + e.getMessage());
        }
    }
} 