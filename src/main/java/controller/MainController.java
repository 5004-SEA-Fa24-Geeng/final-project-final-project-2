package controller;

import model.*;
import view.MainView;
import view.ViewState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main implementation of the IController interface for the Influencer Management System.
 * This class serves as the central controller in the MVC architecture, handling all user interactions,
 * managing data flow between the model and view, and controlling application state.
 *
 * <p>The MainController manages user authentication, influencer data operations (search, filter, sort),
 * favorite management, data import/export, and view transitions.</p>
 *
 * <p>It maintains references to the repository, user manager, view, and current user state
 * to provide a cohesive application experience.</p>
 */
public class MainController implements IController {
    private String controllerName;
    private InfluencerRepository repository;
    private UserManager userManager;
    private UserFavorites userFavorites;
    private IExporter exporter;
    private IImporter importer;
    private MainView mainView;
    private User currentUser;
    private List<Influencer> currentWorkingSet;
    private boolean isRunning;

    /**
     * Creates a new MainController with the specified view.
     * Initializes all required components and sets up the controller-view relationship.
     *
     * @param mainView the main view instance to control
     */
    public MainController(MainView mainView) {
        this.controllerName = "Main Controller";
        this.mainView = mainView;
        this.repository = new InfluencerRepository();
        this.userManager = new UserManager();
        this.exporter = null;
        this.currentWorkingSet = new ArrayList<>();
        this.isRunning = false;

        mainView.setController(this);
    }

    /**
     * Initializes the controller by loading data and setting up the view.
     * Attempts to load data from the default file path, or loads sample data if file loading fails.
     */
    @Override
    public void initialize() {
        if (!tryLoadDataFromFile()) {
            loadSampleData();
        }

        mainView.setVisible(true);
    }

    /**
     * Runs the main application loop.
     * Continuously processes user input until the application is stopped.
     */
    public void run() {
        isRunning = true;

        while (isRunning) {
            String input = mainView.getUserInput();

            try {
                processInputForCurrentState(input);
            } catch (Exception e) {
                mainView.showError("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Processes user input based on the current view state.
     * Delegates to specific state handler methods.
     *
     * @param input the user input string
     */
    private void processInputForCurrentState(String input) {
        Map<String, Object> params = new HashMap<>();
        ViewState currentState = mainView.getCurrentState();

        switch (currentState) {
            case LOGIN:
                handleLoginState(input, params);
                break;

            case REGISTRATION:
                handleRegistrationState(input, params);
                break;

            case USER_PROFILE:
                handleUserProfileState(input, params);
                break;

            case INFLUENCER_LIST:
                handleInfluencerListState(input, params);
                break;

            case USER_FAVORITES:
                handleFavoritesState(input, params);
                break;

            case EXPORT:
                handleExportState(input, params);
                break;

            case IMPORT:
                handleImportState(input, params);
                break;
        }
    }

    /**
     * Stops the application.
     * Sets the running flag to false, which will cause the main loop to exit.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Attempts to load influencer data from the default data file.
     *
     * @return true if data was successfully loaded, false otherwise
     */
    private boolean tryLoadDataFromFile() {
        String defaultDataPath = "src/main/resources/data/influencers.csv";
        importer = new CSVImporter();
        List<Influencer> importedData = importer.importData(defaultDataPath);

        if (importedData != null && !importedData.isEmpty()) {
            for (Influencer influencer : importedData) {
                repository.save(influencer);
            }
            currentWorkingSet = new ArrayList<>(importedData);
            System.out.println("[INFO] Loaded " + importedData.size() + " influencers from " + defaultDataPath);
            return true;
        }

        return false;
    }

    /**
     * Handles requests from the view with specific actions and parameters.
     * This is the main entry point for processing user actions, including login, registration,
     * search, filtering, sorting, and data import/export operations.
     *
     * @param action the action to perform (e.g., "login", "search", "filter")
     * @param params a map of parameters required for the action
     */
    @Override
    public void handleRequest(String action, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        switch (action) {
            case "login":
                String username = (String) params.get("username");
                String password = (String) params.get("password");
                User loggedInUser = handleUserLogin(username, password);
                if (loggedInUser != null) {
                    mainView.showUserProfile(loggedInUser);
                } else {
                    mainView.showError("Invalid username or password.");
                    mainView.showLoginForm();
                }
                break;

            case "register":
                User user = (User) params.get("user");
                try {
                    handleUserRegistration(user);
                    mainView.showLoginForm();
                } catch (IllegalArgumentException e) {
                    mainView.showError(e.getMessage());
                    mainView.showRegistrationForm();
                }
                break;

            case "logout":
                handleUserLogout();
                break;

            case "subscribe":
                handleSubscription();
                mainView.showUserProfile(currentUser);
                break;

            case "unsubscribe":
                handleUnsubscription();
                mainView.showUserProfile(currentUser);
                break;

            case "search":
                String query = (String) params.get("query");
                handleInfluencerSearch(query);
                break;

            case "filter":
                handleInfluencerFilter(params);
                break;

            case "sort":
                String criteria = (String) params.get("criteria");
                boolean ascending = Boolean.TRUE.equals(params.get("ascending"));
                handleInfluencerSort(criteria, ascending);
                break;

            case "addToFavorites":
                if (params.containsKey("influencer")) {
                    handleAddToFavorites((Influencer) params.get("influencer"));
                } else {
                    mainView.showError("Missing influencer object");
                }
                break;

            case "removeFromFavorites":
                if (params.containsKey("influencer")) {
                    handleRemoveFromFavorites((Influencer) params.get("influencer"));
                } else {
                    mainView.showError("Missing influencer object");
                }
                break;

            case "export":
                String exportFormat = (String) params.get("format");
                String exportPath = (String) params.get("path");
                List<Influencer> data;

                // Check if we're exporting favorites
                if (params.containsKey("exportingFavorites") && (boolean) params.get("exportingFavorites")) {
                    // Get favorites directly from the controller's userFavorites instance
                    if (userFavorites == null) {
                        mainView.showExportError("No favorites to export");
                        return;
                    }
                    data = userFavorites.getAllItems();
                    System.out.println("[DEBUG] Exporting " + data.size() + " favorites through handleRequest");
                } else {
                    // Make sure we have the current working set
                    if (currentWorkingSet == null || currentWorkingSet.isEmpty()) {
                        loadAllInfluencers();
                    }
                    data = currentWorkingSet;
                    System.out.println("[DEBUG] Exporting " + data.size() + " influencers through handleRequest");
                }

                // If data was directly provided, use that instead
                if (params.containsKey("data")) {
                    data = (List<Influencer>) params.get("data");
                    System.out.println("[DEBUG] Using provided data: " + data.size() + " items");
                }

                handleExport(exportFormat, exportPath, data);
                break;

            case "import":
                String importFormat = (String) params.get("format");
                String importPath = (String) params.get("path");
                handleImport(importFormat, importPath);
                break;

            case "exit":
                stop();
                break;

            default:
                mainView.showError("Unknown action: " + action);
                break;
        }
    }

    /**
     * Handles user input in the login state.
     * Processes numeric options for login, registration, or exit.
     *
     * @param input the user input string
     * @param params parameters to store and pass during processing
     */
    public void handleLoginState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            mainView.showError("Please enter a valid number");
            return;
        }

        switch (option) {
            case 1:
                String username = mainView.promptForInput("Username: ");
                String password = mainView.promptForInput("Password: ");

                params.put("username", username);
                params.put("password", password);
                handleRequest("login", params);
                break;

            case 2:
                mainView.showRegistrationForm();
                break;

            case 3:
                handleRequest("exit", null);
                break;

            default:
                mainView.showError("Invalid option");
                break;
        }
    }

    /**
     * Handles user input in the registration state.
     * Processes username and password input for creating a new user account.
     *
     * @param input the user input string (username)
     * @param params parameters to store and pass during processing
     */
    public void handleRegistrationState(String input, Map<String, Object> params) {
        String username = input.trim();

        if (username.isEmpty()) {
            mainView.showError("Username cannot be empty");
            mainView.showRegistrationForm();
            return;
        }

        String password = mainView.promptForInput("Password: ");

        if (password.isEmpty()) {
            mainView.showError("Password cannot be empty");
            mainView.showRegistrationForm();
            return;
        }

        params.put("user", new model.User(username, password));
        handleRequest("register", params);
    }

    /**
     * Handles user input in the user profile state.
     * Processes numeric options for subscription management, viewing influencers,
     * viewing favorites, or logging out.
     *
     * @param input the user input string
     * @param params parameters to store and pass during processing
     */
    public void handleUserProfileState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showUserView();
            return;
        }

        switch (option) {
            case 1:
                handleRequest("subscribe", params);
                break;

            case 2:
                showInfluencerListView();
                break;

            case 3:
                showUserFavoritesView();
                break;

            case 4:
                handleRequest("logout", params);
                break;

            default:
                showUserView();
                break;
        }
    }

    /**
     * Handles user input in the influencer list state.
     * Processes numeric options for searching, filtering, sorting, managing favorites,
     * and navigating to other views.
     *
     * @param input the user input string
     * @param params parameters to store and pass during processing
     */
    public void handleInfluencerListState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showInfluencerListView();
            return;
        }

        switch (option) {
            case 1:
                String query = mainView.promptForInput("Enter name to search (in current results): ");
                params.put("query", query);
                handleRequest("search", params);
                showInfluencerListView();
                break;

            case 2:
                String platform = mainView.promptForInput("Enter platform to filter by (in current results): ");
                params.put("platform", platform);
                handleRequest("filter", params);
                break;

            case 3:
                String category = mainView.promptForInput("Enter category to filter by (in current results): ");
                params.put("category", category);
                handleRequest("filter", params);
                break;

            case 4:
                int min = Integer.parseInt(mainView.promptForInput("Enter minimum followers (for current results): "));
                int max = Integer.parseInt(mainView.promptForInput("Enter maximum followers (0 for no limit): "));
                params.put("minFollowers", min);
                params.put("maxFollowers", max);
                handleRequest("filter", params);
                break;

            case 5:
                String country = mainView.promptForInput("Enter country to filter by (in current results): ");
                params.put("country", country);
                handleRequest("filter", params);
                break;

            case 6:
                mainView.displayMessage("Sorting current results by name (A-Z)...");
                params.put("criteria", "name");
                params.put("ascending", true);
                handleRequest("sort", params);
                break;

            case 7:
                mainView.displayMessage("Sorting current results by followers (high to low)...");
                params.put("criteria", "followers");
                params.put("ascending", false);
                handleRequest("sort", params);
                break;

            case 8:
                mainView.displayMessage("Sorting current results by ad rate (high to low)...");
                params.put("criteria", "adRate");
                params.put("ascending", false);
                handleRequest("sort", params);
                break;

            case 9:
                int id = Integer.parseInt(mainView.promptForInput("Enter ID of influencer to add to favorites: ")) - 1;
                List<Influencer> currentInfluencers = mainView.getCurrentInfluencers();

                if (id >= 0 && id < currentInfluencers.size()) {
                    Influencer influencerToAdd = currentInfluencers.get(id);
                    params.put("influencer", influencerToAdd);
                    handleRequest("addToFavorites", params);
                } else {
                    mainView.showError("Invalid influencer ID");
                }
                showInfluencerListView();
                break;

            case 10:
                showExportView();
                break;

            case 11:
                showImportView();
                break;

            case 12:
                showUserView();
                break;

            case 13:
                mainView.displayMessage("Resetting to all influencers...");
                resetWorkingSet();
                showInfluencerListView();
                break;

            default:
                showInfluencerListView();
                break;
        }
    }

    /**
     * Handles user input in the favorites state.
     * Processes numeric options for searching favorites, removing items from favorites,
     * exporting favorites, and navigating back to the user profile.
     *
     * @param input the user input string
     * @param params parameters to store and pass during processing
     */
    public void handleFavoritesState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showUserFavoritesView();
            return;
        }

        switch (option) {
            case 1:
                String name = mainView.promptForInput("Enter name to search: ");
                handleFavoritesSearch(name);
                showUserFavoritesView();
                break;

            case 2:
                int id = Integer.parseInt(mainView.promptForInput("Enter ID of favorite to remove: ")) - 1;
                List<Influencer> favorites = mainView.getCurrentFavorites();

                if (id >= 0 && id < favorites.size()) {
                    Influencer influencerToRemove = favorites.get(id);
                    params.put("influencer", influencerToRemove);
                    handleRequest("removeFromFavorites", params);
                } else {
                    mainView.showError("Invalid favorite ID");
                }
                showUserFavoritesView();
                break;

            case 3:
                exportFavorites();
                break;

            case 4:
                showUserView();
                break;

            default:
                showUserFavoritesView();
                break;
        }
    }

    /**
     * Directly exports the current user's favorites to a file.
     * This method bypasses the parameter passing and exports favorites using the appropriate format.
     * Handles format selection, path input, and export operation.
     */
    private void exportFavorites() {
        if (userFavorites == null) {
            mainView.showExportError("No favorites to export");
            return;
        }

        List<Influencer> favorites = userFavorites.getAllItems();
        if (favorites.isEmpty()) {
            mainView.showExportError("No favorites to export");
            return;
        }

        // Show export format options
        mainView.showExportView();
        String option = mainView.getUserInput();
        int formatOption;
        try {
            formatOption = Integer.parseInt(option);
        } catch (NumberFormatException e) {
            mainView.showError("Invalid option");
            return;
        }

        if (formatOption != 1 && formatOption != 2) {
            if (formatOption == 3) {
                showUserFavoritesView();
            } else {
                mainView.showError("Invalid option");
            }
            return;
        }

        String format = formatOption == 1 ? "csv" : "json";
        String defaultPath = "favorites." + format.toLowerCase();
        String path = mainView.promptForInput("Enter path to save file [" + defaultPath + "]: ");
        if (path.trim().isEmpty()) {
            path = defaultPath;
        }

        // Create exporter and export directly
        IExporter exporter = getExporterForFormat(format);
        if (exporter == null) {
            mainView.showExportError("Unsupported export format: " + format);
            return;
        }

        boolean success = exporter.export(favorites, path);
        if (success) {
            mainView.showExportSuccess(path);
        } else {
            mainView.showExportError("Failed to export data");
        }

        showUserFavoritesView();
    }

    /**
     * Handles the export state of the application.
     * Processes user input for exporting data in CSV or JSON format.
     * Manages format selection, path input, and triggering the actual export operation.
     *
     * @param input the user input string
     * @param params parameters for the export operation
     */
    public void handleExportState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showExportView();
            return;
        }

        boolean isFavorites = params.containsKey("exportingFavorites") && (boolean) params.get("exportingFavorites");

        switch (option) {
            case 1:
            case 2:
                String format = option == 1 ? "csv" : "json";
                String defaultPath = "export." + format.toLowerCase();

                String path = mainView.promptForInput("Enter path to save file [" + defaultPath + "]: ");
                if (path.trim().isEmpty()) {
                    path = defaultPath;
                }

                // Create a new params map to avoid potential parameter issues
                Map<String, Object> exportParams = new HashMap<>();
                exportParams.put("format", format);
                exportParams.put("path", path);
                exportParams.put("exportingFavorites", isFavorites);

                if (isFavorites) {
                    // Get favorites directly from UserFavorites
                    if (userFavorites == null) {
                        mainView.showExportError("No favorites to export");
                        showUserFavoritesView();
                        return;
                    }
                    List<Influencer> favorites = userFavorites.getAllItems();
                    if (favorites.isEmpty()) {
                        mainView.showExportError("No favorites to export");
                        showUserFavoritesView();
                        return;
                    }
                    exportParams.put("data", favorites);
                    handleExport(format, path, favorites);
                } else {
                    loadAllInfluencers();
                    List<Influencer> allInfluencers = mainView.getCurrentInfluencers();
                    exportParams.put("data", allInfluencers);
                    handleExport(format, path, allInfluencers);
                }

                // Don't use handleRequest for export, but call handleExport directly
                // handleRequest("export", exportParams);

                if (isFavorites) {
                    showUserFavoritesView();
                } else {
                    showInfluencerListView();
                }
                break;

            case 3:
                if (isFavorites) {
                    showUserFavoritesView();
                } else {
                    showInfluencerListView();
                }
                break;

            default:
                showExportView();
                break;
        }
    }

    /**
     * Handles the import state of the application.
     * Processes user input for importing data from CSV or JSON files.
     * Manages format selection, path input, and triggering the actual import operation.
     *
     * @param input the user input string
     * @param params parameters for the import operation
     */
    public void handleImportState(String input, Map<String, Object> params) {
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showImportView();
            return;
        }

        switch (option) {
            case 1:
            case 2:
                String format = option == 1 ? "csv" : "json";
                String defaultPath = option == 1 ? "src/main/resources/data/influencers.csv" : "src/main/resources/data/influencers.json";

                String path = mainView.promptForInput("Enter path to import file [" + defaultPath + "]: ");
                if (path.trim().isEmpty()) {
                    path = defaultPath;
                }

                params.put("format", format);
                params.put("path", path);

                handleRequest("import", params);
                showInfluencerListView();
                break;

            case 3:
                showInfluencerListView();
                break;

            default:
                showImportView();
                break;
        }
    }

    /**
     * Gets the name of the controller.
     *
     * @return the name of the controller as a string
     */
    @Override
    public String getControllerName() {
        return controllerName;
    }

    /**
     * Handles user registration process.
     * Delegates to the UserManager to register a new user.
     *
     * @param user the user object containing registration information
     * @throws IllegalArgumentException if the user information is invalid or the username already exists
     */
    @Override
    public void handleUserRegistration(User user) {
        userManager.registerUser(user);
    }

    /**
     * Handles user login process.
     * Authenticates the user with the provided credentials and sets the current user if successful.
     *
     * @param username the username provided by the user
     * @param password the password provided by the user
     * @return the authenticated user object if login is successful, null otherwise
     */
    @Override
    public User handleUserLogin(String username, String password) {
        User user = userManager.authenticateUser(username, password);
        if (user != null) {
            setCurrentUser(user);
        }
        return user;
    }

    /**
     * Handles user logout process.
     * Clears the current user session and returns to the login view.
     */
    @Override
    public void handleUserLogout() {
        setCurrentUser(null);
        mainView.showLoginForm();
    }

    @Override
    public void handleSubscription() {
        validateUser();
        currentUser.subscribe();
        updateViews();
    }

    @Override
    public void handleUnsubscription() {
        validateUser();
        currentUser.unsubscribe();
        updateViews();
    }

    @Override
    public void handleInfluencerSearch(String query) {
        validateUser();

        if (query.isEmpty()) {
            loadAllInfluencers();
            return;
        }

        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : currentWorkingSet) {
            if (influencer.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(influencer);
            }
        }

        currentWorkingSet = results;
        mainView.displaySearchResults(results);
    }

    @Override
    public void handleInfluencerFilter(Map<String, Object> filterCriteria) {
        validateUser();

        if (filterCriteria.containsKey("platform")) {
            String platform = (String) filterCriteria.get("platform");
            currentWorkingSet = repository.filterByPlatform(platform);
        } else if (filterCriteria.containsKey("category")) {
            String category = (String) filterCriteria.get("category");
            currentWorkingSet = repository.filterByCategory(category);
        } else if (filterCriteria.containsKey("minFollowers") || filterCriteria.containsKey("maxFollowers")) {
            int min = filterCriteria.containsKey("minFollowers") ? (int) filterCriteria.get("minFollowers") : 0;
            int max = filterCriteria.containsKey("maxFollowers") ? (int) filterCriteria.get("maxFollowers") : Integer.MAX_VALUE;
            currentWorkingSet = repository.filterByFollowerRange(min, max);
        } else if (filterCriteria.containsKey("country")) {
            String country = (String) filterCriteria.get("country");
            currentWorkingSet = repository.filterByCountry(country);
        }

        mainView.displayInfluencers(currentWorkingSet);
    }

    @Override
    public void handleInfluencerSort(String sortCriteria, boolean ascending) {
        validateUser();
        List<Influencer> results = new ArrayList<>(currentWorkingSet);

        switch (sortCriteria) {
            case "name":
                results = sortByName(results, ascending);
                break;
            case "followers":
                results = sortByFollowers(results, ascending);
                break;
            case "adRate":
                validateSubscription();
                results = sortByAdRate(results, ascending);
                break;
            default:
                return;
        }

        currentWorkingSet = results;
        mainView.displayInfluencers(results);
    }

    private List<Influencer> sortByName(List<Influencer> influencers, boolean ascending) {
        List<Influencer> sorted = new ArrayList<>(influencers);
        sorted.sort((a, b) -> {
            int result = a.getName().compareToIgnoreCase(b.getName());
            return ascending ? result : -result;
        });
        return sorted;
    }

    private List<Influencer> sortByFollowers(List<Influencer> influencers, boolean ascending) {
        return influencers.stream()
                .sorted(Comparator.comparingInt(Influencer::getFollowers).reversed())
                .collect(Collectors.toList());
    }

    private List<Influencer> sortByAdRate(List<Influencer> influencers, boolean ascending) {
        List<Influencer> sorted = new ArrayList<>(influencers);
        sorted.sort((a, b) -> {
            int result = Double.compare(a.getAdRate(), b.getAdRate());
            return ascending ? result : -result;
        });
        return sorted;
    }

    @Override
    public void loadAllInfluencers() {
        validateUser();
        List<Influencer> allInfluencers = repository.findAll();
        currentWorkingSet = new ArrayList<>(allInfluencers);
        mainView.displayInfluencers(allInfluencers);
    }

    public void resetWorkingSet() {
        loadAllInfluencers();
    }

    @Override
    public void handleAddToFavorites(Influencer influencer) {
        validateUser();
        if (userFavorites == null) {
            userFavorites = new UserFavorites(currentUser);
        }
        userFavorites.addItem(influencer);
        loadAllFavorites();
    }

    @Override
    public void handleRemoveFromFavorites(Influencer influencer) {
        validateUser();
        if (userFavorites != null) {
            userFavorites.removeItem(influencer);
            loadAllFavorites();
        }
    }

    @Override
    public void loadAllFavorites() {
        validateUser();
        if (userFavorites != null) {
            mainView.displayFavorites(userFavorites.getAllItems());
        }
    }

    @Override
    public void handleFavoritesSearch(String name) {
        validateUser();
        if (userFavorites != null) {
            List<Influencer> results = userFavorites.searchByName(name);
            mainView.displaySearchResults(results);
        }
    }

    /**
     * Handles exporting influencer data to a file.
     * Validates the user, creates the appropriate exporter, and performs the export operation.
     *
     * @param format the export format (e.g., "csv", "json")
     * @param path the file path for the export
     * @param data the list of influencers to export
     * @throws IllegalStateException if no user is logged in
     */
    @Override
    public void handleExport(String format, String path, List<Influencer> data) {
        validateUser();

        exporter = getExporterForFormat(format);
        if (exporter == null) {
            mainView.showExportError("Unsupported export format: " + format);
            return;
        }

        boolean success = exporter.export(data, path);
        if (success) {
            mainView.showExportSuccess(path);
        } else {
            mainView.showExportError("Failed to export data");
        }
    }

    /**
     * Handles importing influencer data from a file.
     * Validates the user, creates the appropriate importer, and performs the import operation.
     *
     * @param format the import format (e.g., "csv", "json")
     * @param path the file path to import from
     * @throws IllegalStateException if no user is logged in
     */
    public void handleImport(String format, String path) {
        validateUser();

        importer = getImporterForFormat(format);
        if (importer == null) {
            mainView.showError("Unsupported import format: " + format);
            return;
        }

        List<Influencer> importedData = importer.importData(path);
        if (importedData.isEmpty()) {
            mainView.showError("Failed to import data or file was empty");
        } else {
            for (Influencer influencer : importedData) {
                repository.save(influencer);
            }

            currentWorkingSet = new ArrayList<>(importedData);
            mainView.showImportSuccess(importedData.size() + " influencers imported");
            loadAllInfluencers();
        }
    }

    /**
     * Gets an appropriate exporter for the specified format.
     *
     * @param format the export format (e.g., "csv", "json")
     * @return an IExporter implementation for the specified format, or null if unsupported
     */
    private IExporter getExporterForFormat(String format) {
        switch (format.toLowerCase()) {
            case "csv":
                return new CSVExporter();
            case "json":
                return new JSONExporter();
            default:
                return null;
        }
    }

    /**
     * Gets an appropriate importer for the specified format.
     *
     * @param format the import format (e.g., "csv", "json")
     * @return an IImporter implementation for the specified format, or null if unsupported
     */
    private IImporter getImporterForFormat(String format) {
        switch (format.toLowerCase()) {
            case "csv":
                return new CSVImporter();
            case "json":
                return new JSONImporter();
            default:
                return null;
        }
    }

    @Override
    public void showInfluencerListView() {
        validateUser();
        loadAllInfluencers();
        mainView.showInfluencerListView();
    }

    @Override
    public void showUserFavoritesView() {
        validateUser();
        loadAllFavorites();
        mainView.showUserFavoritesView();
    }

    @Override
    public void showExportView() {
        validateUser();
        mainView.showExportView();
    }

    @Override
    public void showUserView() {
        validateUser();
        mainView.showUserView();
    }

    @Override
    public void showImportView() {
        validateUser();
        mainView.showImportView();
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        mainView.setCurrentUser(user);

        if (user != null) {
            this.userFavorites = new UserFavorites(user);
            user.setFavorites(userFavorites);
        } else {
            this.userFavorites = null;
        }
    }

    /**
     * Validates that a user is currently logged in.
     *
     * @throws IllegalStateException if no user is logged in
     */
    private void validateUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user logged in");
        }
    }

    /**
     * Validates that the current user has a premium subscription.
     *
     * @throws IllegalStateException if no user is logged in or the user doesn't have a premium subscription
     */
    private void validateSubscription() {
        validateUser();
        if (!currentUser.isSubscribed()) {
            throw new IllegalStateException("Premium subscription required");
        }
    }

    /**
     * Updates all views with current user information.
     */
    private void updateViews() {
        mainView.setCurrentUser(currentUser);
    }

    /**
     * Loads sample data into the repository.
     * Used when no data file is available.
     */
    private void loadSampleData() {
        // Add some sample influencers
        repository.save(new Influencer("John Doe", "Instagram", "Fashion", 100000, 1000.0, "USA"));
        repository.save(new Influencer("Jane Smith", "YouTube", "Beauty", 500000, 2000.0, "UK"));
        repository.save(new Influencer("Mike Johnson", "TikTok", "Gaming", 200000, 1500.0, "Canada"));
        repository.save(new Influencer("Sarah Williams", "Instagram", "Travel", 300000, 2500.0, "Australia"));
        repository.save(new Influencer("David Brown", "YouTube", "Tech", 400000, 3000.0, "USA"));
        repository.save(new Influencer("Emily Davis", "TikTok", "Food", 150000, 1200.0, "UK"));
        repository.save(new Influencer("Robert Wilson", "Instagram", "Fitness", 250000, 1800.0, "Canada"));
        repository.save(new Influencer("Lisa Taylor", "YouTube", "Music", 350000, 2200.0, "Australia"));

        try {
            userManager.registerUser(new User("admin", "admin"));
            userManager.registerUser(new User("premium", "premium"));

            User premiumUser = userManager.findUser("premium");
            premiumUser.subscribe();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Gets the main view associated with this controller.
     *
     * @return the main view instance
     */
    public MainView getMainView() {
        return mainView;
    }

    /**
     * Handles search requests for influencers based on provided search parameters.
     *
     * @param params a map containing search parameters, must include a "name" key
     * @return a list of influencers matching the search criteria
     * @throws IllegalArgumentException if required parameters are missing
     */
    public List<Influencer> handleSearchRequest(Map<String, Object> params) {
        if (params == null || !params.containsKey("name")) {
            throw new IllegalArgumentException("Search requires a name parameter");
        }

        String name = (String) params.get("name");
        return repository.searchByName(name);
    }

    /**
     * Handles filter requests for influencers based on provided filter parameters.
     *
     * @param params a map containing filter parameters, must include a "filterType" key
     *              and appropriate value keys based on the filter type
     * @return a list of influencers matching the filter criteria
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public List<Influencer> handleFilterRequest(Map<String, Object> params) {
        if (params == null || !params.containsKey("filterType")) {
            throw new IllegalArgumentException("Filter requires a filterType parameter");
        }

        String filterType = (String) params.get("filterType");
        switch (filterType) {
            case "platform":
                return repository.filterByPlatform((String) params.get("platform"));
            case "category":
                return repository.filterByCategory((String) params.get("category"));
            case "followers":
                return repository.filterByFollowerRange(
                        (Integer) params.get("minFollowers"),
                        (Integer) params.get("maxFollowers"));
            case "country":
                return repository.filterByCountry((String) params.get("country"));
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType);
        }
    }

    /**
     * Handles sort requests for influencers based on provided sort parameters.
     *
     * @param params a map containing sort parameters, must include a "sortType" key
     * @return a list of influencers sorted according to the specified criteria
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public List<Influencer> handleSortRequest(Map<String, Object> params) {
        if (params == null || !params.containsKey("sortType")) {
            throw new IllegalArgumentException("Sort requires a sortType parameter");
        }

        String sortType = (String) params.get("sortType");
        boolean ascending = params.containsKey("ascending") ? (Boolean) params.get("ascending") : true;

        switch (sortType) {
            case "name":
                return sortByName(currentWorkingSet, ascending);
            case "followers":
                return sortByFollowers(currentWorkingSet, ascending);
            case "adRate":
                return sortByAdRate(currentWorkingSet, ascending);
            default:
                throw new IllegalArgumentException("Invalid sort type: " + sortType);
        }
    }
}


