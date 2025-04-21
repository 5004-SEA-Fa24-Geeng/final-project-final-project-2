package view;

import model.Influencer;
import model.User;

import java.util.List;

/**
 * Interface for the application view component in the MVC architecture.
 * This interface defines the contract for user interface implementations
 * that display influencer data and handle user interactions.
 */
public interface IView {

    /**
     * Renders the current view state to the user interface.
     * This method should be called whenever the view needs to be updated.
     */
    void render();

    /**
     * Updates the view with new data.
     *
     * @param data The data to update the view with, typically a collection of influencers
     */
    void update(Object data);

    /**
     * Sets the visibility state of the view.
     *
     * @param visible true to make the view visible, false to hide it
     */
    void setVisible(boolean visible);

    /**
     * Switches the view to display the influencer list and renders it.
     */
    void showInfluencerListView();

    /**
     * Switches the view to display the user's favorites list and renders it.
     */
    void showUserFavoritesView();

    /**
     * Switches the view to display the export options and renders it.
     */
    void showExportView();

    /**
     * Switches the view to display the user profile and renders it.
     */
    void showUserView();

    /**
     * Switches the view to display the login form and renders it.
     */
    void showLoginForm();

    /**
     * Switches the view to display the registration form and renders it.
     */
    void showRegistrationForm();

    /**
     * Displays the user profile with the specified user data.
     *
     * @param user The user whose profile should be displayed
     */
    void showUserProfile(User user);

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display
     */
    void showError(String message);

    /**
     * Sets the current user for the view.
     *
     * @param user The user to set as the current user
     */
    void setCurrentUser(User user);

    /**
     * Displays a list of influencers in the main view area.
     *
     * @param influencers The list of influencers to display
     */
    void displayInfluencers(List<Influencer> influencers);

    /**
     * Displays search results to the user.
     *
     * @param results The list of influencers matching the search criteria
     */
    void displaySearchResults(List<Influencer> results);

    /**
     * Determines whether ad rates should be displayed based on the user's subscription status.
     *
     * @return true if ad rates should be displayed, false otherwise
     */
    boolean shouldShowAdRate();

    /**
     * Displays the user's favorite influencers.
     *
     * @param favorites The list of favorite influencers to display
     */
    void displayFavorites(List<Influencer> favorites);

    /**
     * Displays export format options to the user.
     */
    void showExportOptions();

    /**
     * Displays a success message after a successful export operation.
     *
     * @param path The path where the data was exported to
     */
    void showExportSuccess(String path);

    /**
     * Displays an error message when an export operation fails.
     *
     * @param error The error message explaining why the export failed
     */
    void showExportError(String error);

    /**
     * Gets the list of influencers currently displayed in the view.
     *
     * @return A list of currently displayed influencers
     */
    List<Influencer> getCurrentInfluencers();

    /**
     * Gets the list of favorite influencers currently displayed in the view.
     *
     * @return A list of currently displayed favorite influencers
     */
    List<Influencer> getCurrentFavorites();

    /**
     * Switches the view to display the import options and renders it.
     */
    void showImportView();

    /**
     * Displays a success message after a successful import operation.
     *
     * @param message The success message with details about the import
     */
    void showImportSuccess(String message);
}