package controller;

import model.Influencer;
import model.User;

import java.util.List;
import java.util.Map;

/**
 * Interface defining the controller component of the MVC architecture for the Influencer Management System.
 * Provides methods for handling user interactions, data management, and view transitions.
 * This interface serves as a contract that all controller implementations must fulfill.
 *
 * <p>The controller acts as an intermediary between the model and view, processing user inputs
 * from the view and updating the model accordingly, then reflecting changes back to the view.</p>
 */
public interface IController {
    /**
     * Gets the name of the controller.
     *
     * @return the name of the controller as a string
     */
    String getControllerName();

    /**
     * Initializes the controller.
     * This includes loading initial data and setting up the view.
     */
    void initialize();

    /**
     * Handles requests from the view with specific actions and parameters.
     * This is the main entry point for processing user actions.
     *
     * @param action the action to perform (e.g., "login", "search", "filter")
     * @param params a map of parameters required for the action
     */
    void handleRequest(String action, Map<String, Object> params);

    /**
     * Handles user registration process.
     *
     * @param user the user object containing registration information
     * @throws IllegalArgumentException if the user information is invalid or the username already exists
     */
    void handleUserRegistration(User user);

    /**
     * Handles user login process.
     *
     * @param username the username provided by the user
     * @param password the password provided by the user
     * @return the authenticated user object if login is successful, null otherwise
     */
    User handleUserLogin(String username, String password);

    /**
     * Handles user logout process.
     * Clears the current user session and returns to the login view.
     */
    void handleUserLogout();

    /**
     * Handles subscription process for the current user.
     * Upgrades the user to premium status.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void handleSubscription();

    /**
     * Handles unsubscription process for the current user.
     * Downgrades the user from premium to free status.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void handleUnsubscription();

    /**
     * Handles searching for influencers by name.
     *
     * @param query the search query (name or partial name)
     * @throws IllegalStateException if no user is logged in
     */
    void handleInfluencerSearch(String query);

    /**
     * Handles filtering influencers based on various criteria.
     *
     * @param filterCriteria a map containing filter criteria (platform, category, followers, country)
     * @throws IllegalStateException if no user is logged in
     */
    void handleInfluencerFilter(Map<String, Object> filterCriteria);

    /**
     * Handles sorting influencers based on specified criteria.
     *
     * @param sortCriteria the field to sort by (name, followers, adRate)
     * @param ascending true for ascending order, false for descending
     * @throws IllegalStateException if no user is logged in
     * @throws IllegalStateException if premium features are accessed by non-premium users
     */
    void handleInfluencerSort(String sortCriteria, boolean ascending);

    /**
     * Loads all influencers from the repository and displays them.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void loadAllInfluencers();

    /**
     * Handles adding an influencer to the current user's favorites.
     *
     * @param influencer the influencer to add to favorites
     * @throws IllegalStateException if no user is logged in
     */
    void handleAddToFavorites(Influencer influencer);

    /**
     * Handles removing an influencer from the current user's favorites.
     *
     * @param influencer the influencer to remove from favorites
     * @throws IllegalStateException if no user is logged in
     */
    void handleRemoveFromFavorites(Influencer influencer);

    /**
     * Loads all favorites for the current user and displays them.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void loadAllFavorites();

    /**
     * Handles searching for favorites by name.
     *
     * @param name the search query (name or partial name)
     * @throws IllegalStateException if no user is logged in
     */
    void handleFavoritesSearch(String name);

    /**
     * Handles exporting influencer data to a file.
     *
     * @param format the export format (e.g., "csv", "json")
     * @param path the file path for the export
     * @param data the list of influencers to export
     * @throws IllegalStateException if no user is logged in
     */
    void handleExport(String format, String path, List<Influencer> data);

    /**
     * Shows the influencer list view.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void showInfluencerListView();

    /**
     * Shows the user favorites view.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void showUserFavoritesView();

    /**
     * Shows the export view.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void showExportView();

    /**
     * Shows the user profile view.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void showUserView();

    /**
     * Shows the import view.
     *
     * @throws IllegalStateException if no user is logged in
     */
    void showImportView();

    /**
     * Sets the current user and updates related components.
     *
     * @param user the user to set as current, or null to clear the current user
     */
    void setCurrentUser(User user);
}