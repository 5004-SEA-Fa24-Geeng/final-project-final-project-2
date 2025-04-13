package view;

import model.Influencer;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the MainView class.
 */
public class MainViewTest {

    private MainView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // Redirect System.out to a custom PrintStream (outContent)
        // to capture standard output programmatically
        System.setOut(new PrintStream(outContent));

        view = new MainView();

        // Create a test user
        User testUser = new User("testUser", "password");
        view.setCurrentUser(testUser);

        // create testing data on Influencer
        List<Influencer> testInfluencers = new ArrayList<>();
        testInfluencers.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        testInfluencers.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));

        view.displayInfluencers(testInfluencers);

        // create testing data on favorite influencers
        List<Influencer> testFavorites = new ArrayList<>();
        testFavorites.add(new Influencer("David Lee", "TikTok", "Comedy", 1500000, "Canada", 3000.0));

        view.displayFavorites(testFavorites);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testGetCurrentInfluencers(){
        // test getCurrentInfluencers method
        List<Influencer> currentInfluencers = view.getCurrentInfluencers();

        assertEquals(2, currentInfluencers.size());
        assertEquals("John Smith", currentInfluencers.get(0).getName());
        assertEquals("Emma Johnson", currentInfluencers.get(1).getName());

        // verify if the returned list is a copy (modifications won't affect the original)
        currentInfluencers.add(new Influencer("Test", "Test", "Test", 100, "Test", 100.0));
        assertEquals(2, view.getCurrentInfluencers().size()); // the original list should not change
    }

    @Test
    public void testGetCurrentFavorites() {
        // test getCurrentFavorites method
        List<Influencer> currentFavorites = view.getCurrentFavorites();

        assertEquals(1, currentFavorites.size());
        assertEquals("David Lee", currentFavorites.get(0).getName());

        // verify if the returned list is a copy
        currentFavorites.add(new Influencer("Test", "Test", "Test", 100, "Test", 100.0));
        assertEquals(1, view.getCurrentFavorites().size()); // 原列表应该不变
    }


    @Test
    public void testViewStateTransitions() {
        assertEquals(MainView.ViewState.LOGIN, view.getCurrentState());

        // test switching to influencer list view
        view.showInfluencerListView();
        assertEquals(MainView.ViewState.INFLUENCER_LIST, view.getCurrentState());

        // test switching to favorites view
        view.showUserFavoritesView();
        assertEquals(MainView.ViewState.USER_FAVORITES, view.getCurrentState());

        // test switching to export view
        view.showExportView();
        assertEquals(MainView.ViewState.EXPORT, view.getCurrentState());

        // test switching to import view
        view.showImportView();
        assertEquals(MainView.ViewState.IMPORT, view.getCurrentState());

        // test switching to user profile view
        view.showUserView();
        assertEquals(MainView.ViewState.USER_PROFILE, view.getCurrentState());
    }

    @Test
    public void testRenderInfluencerListMenu() {
        outContent.reset();

        view.setVisible(true);
        view.showInfluencerListView();

        String output = outContent.toString();
        assertTrue(output.contains("13. Reset to All Influencers"));
    }


}
