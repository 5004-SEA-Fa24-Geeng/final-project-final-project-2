package view;

import model.Influencer;
import model.User;
import java.util.List;

public class ConsoleView implements IView {
    private User currentUser;
    private List<Influencer> currentInfluencers;
    private List<Influencer> currentFavorites;
    private ViewState currentState;

    public ConsoleView() {
        // Default state is LOGIN.
        this.currentState = ViewState.LOGIN;
    }

    // Return the current view state.
    public ViewState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ViewState state) {
        this.currentState = state;
    }

    @Override
    public void render() {
        if (currentState == ViewState.LOGIN) {
            System.out.println("==== Welcome to the Influencer Management System ====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("Enter the corresponding number or type 'exit' to quit:");
        } else if (currentState == ViewState.MAIN_MENU) {
            System.out.println("==== Main Menu ====");
            System.out.println("1. View All Influencers");
            System.out.println("2. Search Influencers");
            System.out.println("3. Sort Influencers");
            System.out.println("4. Filter Influencers");
            System.out.println("5. Add Influencer to Favorites");
            System.out.println("6. View Favorites");
            System.out.println("7. Logout");
            if (isUserSubscribed()) {
                System.out.println("8. Unsubscribe from Premium");
            } else {
                System.out.println("8. Subscribe to Premium");
            }
            System.out.println("Enter the corresponding number or type 'exit' to quit:");
        }
    }

    @Override
    public void update(Object data) {
        // Optionally update the display if needed.
    }

    @Override
    public void setVisible(boolean visible) {
        // Not used for console applications.
    }

    @Override
    public void showInfluencerListView() {
        System.out.println("==== Influencer List ====");
        if (currentInfluencers == null || currentInfluencers.isEmpty()) {
            System.out.println("No influencers available.");
        } else {
            for (int i = 0; i < currentInfluencers.size(); i++) {
                Influencer inf = currentInfluencers.get(i);
                System.out.printf("%d. %s | %s | %d followers | %s\n",
                        i + 1, inf.getName(), inf.getPlatform(), inf.getFollowerCount(), inf.getCountry());
                if (shouldShowAdRate()) {
                    System.out.println("   Ad Rate: $" + inf.getAdRate());
                }
            }
        }
    }

    @Override
    public void showUserFavoritesView() {
        System.out.println("==== My Favorites ====");
        if (currentFavorites == null || currentFavorites.isEmpty()) {
            System.out.println("No favorites available.");
        } else {
            for (int i = 0; i < currentFavorites.size(); i++) {
                Influencer inf = currentFavorites.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, inf.getName(), inf.getPlatform());
            }
        }
    }

    @Override
    public void showExportView() {
        System.out.println("==== Export Data ====");
        System.out.println("Example: export csv /path/to/file.csv");
        System.out.println("Example: export json /path/to/file.json");
    }

    @Override
    public void showUserView() {
        System.out.println("==== User Information ====");
        if (currentUser == null) {
            System.out.println("No user logged in.");
        } else {
            System.out.println("Username: " + currentUser.getUsername());
            System.out.println("Subscription: " + (currentUser.isSubscribed() ? "Premium" : "Free"));
        }
    }

    @Override
    public void showLoginForm() {
        setCurrentState(ViewState.LOGIN);
        System.out.println("Please select: 1) Login   2) Register or type 'exit' to quit.");
    }

    @Override
    public void showRegistrationForm() {
        System.out.println("==== Registration ====");
        System.out.println("Please enter the required details.");
    }

    @Override
    public void showUserProfile(User user) {
        System.out.println("==== User Profile ====");
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User data not available.");
        }
    }

    @Override
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            setCurrentState(ViewState.MAIN_MENU);
        }
    }

    @Override
    public void displayInfluencers(List<Influencer> influencers) {
        this.currentInfluencers = influencers;
        showInfluencerListView();
    }

    @Override
    public void displaySearchResults(List<Influencer> results) {
        System.out.println("==== Search Results ====");
        if (results == null || results.isEmpty()) {
            System.out.println("No matching influencers found.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                Influencer inf = results.get(i);
                System.out.printf("%d. %s\n", i + 1, inf.getName());
            }
        }
    }

    @Override
    public boolean shouldShowAdRate() {
        return currentUser != null && currentUser.isSubscribed();
    }

    @Override
    public void displayFavorites(List<Influencer> favorites) {
        this.currentFavorites = favorites;
        showUserFavoritesView();
    }

    @Override
    public void showExportOptions() {
        showExportView();
    }

    @Override
    public void showExportSuccess(String path) {
        System.out.println("Data successfully exported to: " + path);
    }

    @Override
    public void showExportError(String error) {
        System.err.println("Export failed: " + error);
    }

    @Override
    public List<Influencer> getCurrentInfluencers() {
        return currentInfluencers;
    }

    @Override
    public List<Influencer> getCurrentFavorites() {
        return currentFavorites;
    }

    @Override
    public void showImportView() {
        System.out.println("==== Import Data ====");
        System.out.println("Example: import csv /path/to/file.csv");
        System.out.println("Example: import json /path/to/file.json");
    }

    @Override
    public void showImportSuccess(String message) {
        System.out.println("Import successful: " + message);
    }

    // New helper method to check subscription status.
    public boolean isUserSubscribed() {
        return currentUser != null && currentUser.isSubscribed();
    }
}
