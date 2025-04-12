import controller.MainController;
import model.Influencer;
import view.MainView;
import view.MainView.ViewState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for the Influencer Management System.
 */
public class Main {

    /**
     * Main method to start the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create view and controller
        MainView view = new MainView();
        MainController controller = new MainController(view);

        // Initialize the controller
        controller.initialize();

        // Main application loop
        boolean running = true;
        Scanner scanner = view.getScanner();

        while (running) {
            String input = scanner.nextLine();
            Map<String, Object> params = new HashMap<>();

            try {
                switch (view.getCurrentState()) {
                    case LOGIN:
                        handleLoginState(input, controller, view, params);
                        break;

                    case REGISTRATION:
                        handleRegistrationState(input, controller, view, params);
                        break;

                    case USER_PROFILE:
                        running = handleUserProfileState(input, controller, params);
                        break;

                    case INFLUENCER_LIST:
                        handleInfluencerListState(input, controller, scanner, params);
                        break;

                    case USER_FAVORITES:
                        handleFavoritesState(input, controller, scanner, params);
                        break;

                    case EXPORT:
                        handleExportState(input, controller, scanner, params);
                        break;

                    case IMPORT:
                        handleImportState(input, controller, scanner, params);
                        break;
                }

            } catch (Exception e) {
                view.showError("Error: " + e.getMessage());
            }

        }

        System.out.println("Thank you for using the Influencer Management System. Goodbye!");
    }



    /**
     * Handles user input in the login state.
     */
    private static void handleLoginState(String input, MainController controller, MainView view, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            view.showError("Please enter a valid number");
            return;
        }

        switch (option) {
            case 1:
                System.out.print("Username: ");
                String username = view.getScanner().nextLine();
                System.out.print("Password: ");
                String password = view.getScanner().nextLine();

                params.put("username", username);
                params.put("password", password);
                controller.handleRequest("login", params);
                break;

            case 2:
                view.showRegistrationForm();
                break;

            case 3:
                System.exit(0);
                break;

            default:
                view.showError("Invalid option");
                break;
        }
    }


    /**
     * Handles user input in the registration state.
     */
    private static void handleRegistrationState(String input, MainController controller, MainView view, Map<String, Object> params) {
        String username = input.trim();

        if (username.isEmpty()) {
            view.showError("Username cannot be empty");
            view.showRegistrationForm();
            return;
        }

        System.out.print("Password: ");
        String password = view.getScanner().nextLine();

        if (password.isEmpty()) {
            view.showError("Password cannot be empty");
            view.showRegistrationForm();
            return;
        }

        params.put("user", new model.User(username, password));
        controller.handleRequest("register", params);
    }


    /**
     * Handles user input in the user profile state.
     *
     * @return false if the application should exit, true otherwise
     */
    private static boolean handleUserProfileState(String input, MainController controller, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            controller.showUserView();
            return true;
        }

        switch (option) {
            case 1:
                controller.handleRequest("subscribe", params);
                break;

            case 2:
                controller.showInfluencerListView();
                break;

            case 3:
                controller.showUserFavoritesView();
                break;

            case 4:
                controller.handleRequest("logout", params);
                break;

            default:
                controller.showUserView();
                break;
        }
        return true;
    }


    /**
     * Handles user input in the influencer list state.
     */
    private static void handleInfluencerListState(String input, MainController controller, Scanner scanner, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            controller.showInfluencerListView();
            return;
        }

        switch (option) {
            case 1:
                System.out.print("Enter name to search (in current results): ");
                String query = scanner.nextLine();
                params.put("query", query);
                controller.handleRequest("search", params);
                controller.showInfluencerListView();
                break;

            case 2:
                System.out.print("Enter platform to filter by (in current results): ");
                String platform = scanner.nextLine();
                params.put("platform", platform);
                controller.handleRequest("filter", params);
                break;

            case 3:
                System.out.print("Enter category to filter by (in current results): ");
                String category = scanner.nextLine();
                params.put("category", category);
                controller.handleRequest("filter", params);
                break;

            case 4:
                System.out.print("Enter minimum followers (for current results): ");
                int min = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter maximum followers (0 for no limit): ");
                int max = Integer.parseInt(scanner.nextLine());
                params.put("minFollowers", min);
                params.put("maxFollowers", max);
                controller.handleRequest("filter", params);
                break;

            case 5:
                System.out.print("Enter country to filter by (in current results): ");
                String country = scanner.nextLine();
                params.put("country", country);
                controller.handleRequest("filter", params);
                break;

            case 6:
                System.out.println("Sorting current results by name (A-Z)...");
                params.put("criteria", "name");
                params.put("ascending", true);
                controller.handleRequest("sort", params);
                break;

            case 7:
                System.out.println("Sorting current results by followers (high to low)...");
                params.put("criteria", "followers");
                params.put("ascending", false);
                controller.handleRequest("sort", params);
                break;

            case 8:
                System.out.println("Sorting current results by ad rate (high to low)...");
                params.put("criteria", "adRate");
                params.put("ascending", false);
                controller.handleRequest("sort", params);
                break;

            case 9:
                System.out.print("Enter ID of influencer to add to favorites: ");
                int id = Integer.parseInt(scanner.nextLine()) - 1;

                // get the currently displayed influencers list
                List<Influencer> currentInfluencers = controller.getMainView().getCurrentInfluencers();

                // Check the validity of the id
                if (id >= 0 && id < currentInfluencers.size()) {

                    Influencer influencerToAdd = currentInfluencers.get(id);
                    controller.handleAddToFavorites(influencerToAdd);

                } else {
                    controller.getMainView().showError("Invalid influencer ID");
                }

                controller.showInfluencerListView();
                break;

            case 10:
                controller.showExportView();
                break;

            case 11:
                controller.showImportView();
                break;


            case 12:
                controller.showUserView();
                break;

            case 13:
                System.out.println("Resetting to all influencers...");
                controller.resetWorkingSet();
                controller.showInfluencerListView();
                break;

            default:
                controller.showInfluencerListView();
                break;
        }
    }



    /**
     * Handles user input in the favorites state.
     */
    private static void handleFavoritesState(String input, MainController controller, Scanner scanner, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            controller.showUserFavoritesView();
            return;
        }


        switch (option) {
            case 1:
                System.out.print("Enter name to search: ");
                String name = scanner.nextLine();
                controller.handleFavoritesSearch(name);
                controller.showUserFavoritesView();
                break;

            case 2:
                System.out.print("Enter ID of favorite to remove: ");
                // Turn to 0-based index
                int id = Integer.parseInt(scanner.nextLine()) - 1;

                // Get the currently displayed favorites list
                List<Influencer> favorites = controller.getMainView().getCurrentFavorites();

                // Check the validity of the id
                if (id >= 0 && id < favorites.size()) {

                    Influencer influencerToRemove = favorites.get(id);
                    controller.handleRemoveFromFavorites(influencerToRemove);
                } else {
                    controller.getMainView().showError("Invalid favorite ID");
                }

                controller.showUserFavoritesView();
                break;

            case 3:
                controller.showExportView();
                params.put("exportingFavorites", true);
                break;

            case 4:
                controller.showUserView();
                break;

            default:
                controller.showUserFavoritesView();
                break;
        }
    }

    /**
     * Handles user input in the export state.
     */
    private static void handleExportState(String input, MainController controller, Scanner scanner, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            controller.showExportView();
            return;
        }

        boolean isFavorites = params.containsKey("exportingFavorites") && (boolean) params.get("exportingFavorites");

        switch (option) {
            case 1:
            case 2:
                String format = option == 1 ? "csv" : "json";
                String defaultPath = "export." + format.toLowerCase();

                System.out.print("Enter path to save file [" + defaultPath + "]: ");
                String path = scanner.nextLine();

                if (path.trim().isEmpty()) {
                    path = defaultPath;
                }

                params.put("format", format);
                params.put("path", path);

                // Get the appropriate data to export
                if (isFavorites) {
                    controller.loadAllFavorites();
                    // We need to actually pass the favorites data here
                    // This would be better done through the controller API
                } else {
                    controller.loadAllInfluencers();
                }

                controller.handleRequest("export", params);


                if (isFavorites) {
                    controller.showUserFavoritesView();
                } else {
                    controller.showInfluencerListView();
                }
                break;

            case 3:
                if (isFavorites) {
                    controller.showUserFavoritesView();
                } else {
                    controller.showInfluencerListView();
                }
                break;

            default:
                controller.showExportView();
                break;
        }
    }


    /**
     * Handles user input in the import state.
     */
    private static void handleImportState(String input, MainController controller, Scanner scanner, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            controller.showImportView();
            return;
        }

        switch (option) {
            case 1:
            case 2:
                String format = option == 1 ? "csv" : "json";
                String defaultPath = option == 1 ? "src/main/resources/data/influencers.csv" : "src/main/resources/data/influencers.json";

                System.out.print("Enter path to import file [" + defaultPath + "]: ");
                String path = scanner.nextLine();

                if (path.trim().isEmpty()) {
                    path = defaultPath;
                }

                params.put("format", format);
                params.put("path", path);

                controller.handleRequest("import", params);
                controller.showInfluencerListView();
                break;

            case 3:
                controller.showInfluencerListView();
                break;

            default:
                controller.showImportView();
                break;
        }
    }
}
