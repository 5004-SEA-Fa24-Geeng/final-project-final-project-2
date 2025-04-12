package view;

import model.Influencer;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main view implementation for the console-based user interface.
 */

public class MainView implements IView {
    private User currentUser;
    private boolean isVisible;
    private List<Influencer> influencers;
    private List<Influencer> favorites;
    private Scanner scanner;

    public enum ViewState {
        INFLUENCER_LIST,
        USER_FAVORITES,
        EXPORT,
        IMPORT,
        USER_PROFILE,
        LOGIN,
        REGISTRATION
    }

    private ViewState currentState;

    /**
     * Constructs a new MainView.
     */
    public MainView() {
        this.isVisible = false;
        this.influencers = new ArrayList<>();
        this.favorites = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.currentState = ViewState.LOGIN;
    }


    @Override
    public void render() {
        if (!isVisible) {
            return;
        }

        System.out.println("\n==== Influencer Management System ====\n");

        switch (currentState) {
            case LOGIN:
                renderLoginView();
                break;
            case REGISTRATION:
                renderRegistrationView();
                break;
            case USER_PROFILE:
                renderUserProfileView();
                break;
            case INFLUENCER_LIST:
                renderInfluencerListView();
                break;
            case USER_FAVORITES:
                renderFavoritesView();
                break;
            case EXPORT:
                renderExportView();
                break;
            case IMPORT:
                renderImportView();
                break;
        }
    }


    @Override
    public void update(Object data) {
        if (data instanceof List) {
            List<?> dataList = (List<?>) data;
            if (!dataList.isEmpty() && dataList.get(0) instanceof Influencer) {
                this.influencers = new ArrayList<>((List<Influencer>) data);
                updateInfluencerDisplay();
            }
        }
    }


    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        if (visible) {
            render();
        }
    }

    @Override
    public void showInfluencerListView() {
        this.currentState = ViewState.INFLUENCER_LIST;
        render();
    }


    @Override
    public void showUserFavoritesView() {
        this.currentState = ViewState.USER_FAVORITES;
        render();
    }

    @Override
    public void showExportView() {
        this.currentState = ViewState.EXPORT;
        render();
    }


    @Override
    public void showUserView() {
        this.currentState = ViewState.USER_PROFILE;
        render();
    }

    @Override
    public void showLoginForm() {
        this.currentState = ViewState.LOGIN;
        render();
    }

    @Override
    public void showRegistrationForm() {
        this.currentState = ViewState.REGISTRATION;
        render();
    }

    @Override
    public void showUserProfile(User user) {
        this.currentUser = user;
        this.currentState = ViewState.USER_PROFILE;
        render();
    }

    @Override
    public void showError(String message) {
        System.out.println("\n[ERROR] " + message);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserDisplay();
    }

    @Override
    public void displayInfluencers(List<Influencer> influencers) {
        this.influencers = new ArrayList<>(influencers);
        updateInfluencerDisplay();
    }

    @Override
    public void displaySearchResults(List<Influencer> results) {
        System.out.println("\n==== Search Results ====");
        displayInfluencerList(results);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    @Override
    public boolean shouldShowAdRate() {
        return currentUser != null && currentUser.isSubscribed();
    }

    @Override
    public void displayFavorites(List<Influencer> favorites) {
        this.favorites = new ArrayList<>(favorites);
        updateFavoritesDisplay();
    }

    @Override
    public void showExportOptions() {
        this.currentState = ViewState.EXPORT;
        render();
    }

    @Override
    public void showExportSuccess(String path) {
        System.out.println("\n[SUCCESS] Data exported successfully to: " + path);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }


    @Override
    public void showExportError(String error) {
        System.out.println("\n[ERROR] Export failed: " + error);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }




    /**
     * Shows the import view.
     */
    public void showImportView() {
        this.currentState = ViewState.IMPORT;
        render();
    }

    /**
     * Shows a success message for a successful import.
     *
     * @param message the success message
     */
    public void showImportSuccess(String message) {
        System.out.println("\n[SUCCESS] " + message);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Updates the influencer display.
     */
    private void updateInfluencerDisplay() {
        if (currentState == ViewState.INFLUENCER_LIST) {
            render();
        }
    }

    /**
     * Updates the favorites display.
     */
    private void updateFavoritesDisplay() {
        if (currentState == ViewState.USER_FAVORITES) {
            render();
        }
    }

    /**
     * Updates the user display.
     */
    private void updateUserDisplay() {
        if (currentState == ViewState.USER_PROFILE) {
            render();
        }
    }


    /**
     * Renders the login view.
     */
    private void renderLoginView() {
        System.out.println("=== Login ===");
        System.out.println("1. Enter Username and Password");
        System.out.println("2. Register a New Account");
        System.out.println("3. Exit");
        System.out.print("Select an option: ");
    }

    /**
     * Renders the registration view.
     */
    private void renderRegistrationView() {
        System.out.println("=== Register a New Account ===");
        System.out.println("Please enter your details:");
        System.out.print("Username: ");
    }

    /**
     * Renders the user profile view.
     */
    private void renderUserProfileView() {
        if (currentUser == null) {
            showError("No user logged in");
            showLoginForm();
            return;
        }

        System.out.println("=== User Profile ===");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Subscription Status: " + (currentUser.isSubscribed() ? "Premium" : "Free"));
        System.out.println("\nOptions:");
        System.out.println("1. " + (currentUser.isSubscribed() ? "Cancel" : "Subscribe to") + " Premium");
        System.out.println("2. View Influencers");
        System.out.println("3. View Favorites");
        System.out.println("4. Logout");
        System.out.print("Select an option: ");
    }

    /**
     * Renders the influencer list view.
     */
    private void renderInfluencerListView() {
        System.out.println("=== Influencer List ===");
        if (influencers.isEmpty()) {
            System.out.println("No influencers to display.");
        } else {
            displayInfluencerList(influencers);
        }

        System.out.println("\nOptions:");
        System.out.println("1. Search by Name");
        System.out.println("2. Filter by Platform");
        System.out.println("3. Filter by Category");
        System.out.println("4. Filter by Follower Range");
        System.out.println("5. Filter by Country");
        System.out.println("6. Sort by Name");
        System.out.println("7. Sort by Followers");
        System.out.println("8. Sort by Ad Rate");
        System.out.println("9. Add to Favorites");
        System.out.println("10. Export Data");
        System.out.println("11. Import Data");
        System.out.println("12. Back to User Profile");
        System.out.println("13. Reset to All Influencers");
        System.out.print("Select an option: ");
    }


    /**
     * Renders the favorites view.
     */
    private void renderFavoritesView() {
        System.out.println("=== Your Favorites ===");
        if (favorites.isEmpty()) {
            System.out.println("No favorites to display.");
        } else {
            displayInfluencerList(favorites);
        }

        System.out.println("\nOptions:");
        System.out.println("1. Search by Name");
        System.out.println("2. Remove from Favorites");
        System.out.println("3. Export Favorites");
        System.out.println("4. Back to User Profile");
        System.out.print("Select an option: ");
    }

    /**
     * Renders the export view.
     */
    private void renderExportView() {
        System.out.println("=== Export Data ===");
        System.out.println("1. Export as CSV");
        System.out.println("2. Export as JSON");
        System.out.println("3. Back");
        System.out.print("Select an option: ");
    }

    /**
     * Renders the import view.
     */
    private void renderImportView() {
        System.out.println("=== Import Data ===");
        System.out.println("1. Import from CSV");
        System.out.println("2. Import from JSON");
        System.out.println("3. Back");
        System.out.print("Select an option: ");
    }

    /**
     * Displays a list of influencers.
     *
     * @param influencers the list of influencers to display
     */
    private void displayInfluencerList(List<Influencer> influencers) {
        if (influencers.isEmpty()) {
            System.out.println("No influencers to display.");
            return;
        }

        System.out.println("\nID | Name | Platform | Category | Followers | Country" +
                (shouldShowAdRate() ? " | Ad Rate" : ""));
        System.out.println("-".repeat(80));


        for (int i = 0; i < influencers.size(); i++) {
            Influencer inf = influencers.get(i);
            System.out.printf("%-3d| %-20s | %-10s | %-15s | %-10d | %-10s",
                    i + 1, inf.getName(), inf.getPlatform(), inf.getCategory(),
                    inf.getFollowerCount(), inf.getCountry());

            if (shouldShowAdRate()) {
                System.out.printf(" | $%.2f", inf.getAdRate());
            }
            System.out.println();
        }
    }

    /**
     * Gets the scanner for input.
     *
     * @return the scanner
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Gets the current state of the view.
     *
     * @return the current view state
     */
    public ViewState getCurrentState() {
        return currentState;
    }

    /**
     * Gets the list of influencers currently displayed in the view.
     *
     * @return the current list of influencers
     */
    public List<Influencer> getCurrentInfluencers() {
        return new ArrayList<>(influencers);
    }

    /**
     * Gets the list of favorites currently displayed in the view.
     *
     * @return the current list of favorites
     */
    public List<Influencer> getCurrentFavorites() {
        return new ArrayList<>(favorites);
    }
}