package controller;

import model.*;
import view.IView;
import java.util.*;

public class MainController implements IController {
    private final String controllerName = "Main Controller";
    private final InfluencerRepository repository;
    private final UserManager userManager;
    private IExporter exporter;
    private IImporter importer;
    private final IView view;
    private User currentUser;
    private List<Influencer> currentWorkingSet;
    private UserFavorites userFavorites;

    public MainController(IView view) {
        this.view = view;
        this.repository = new InfluencerRepository();
        this.userManager = new UserManager();
        this.currentWorkingSet = new ArrayList<>();
    }

    @Override
    public void initialize() {
        if (!tryLoadDataFromFile()) {
            loadSampleData();
        }
        view.showLoginForm();
    }

    // Attempt to load data from a CSV file
    private boolean tryLoadDataFromFile() {
        String defaultDataPath = "src/main/resources/data/influencers.csv";
        importer = new CSVImporter(); // or new JSONImporter() if using JSON
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

    @Override
    public void handleRequest(String action, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        switch (action.toLowerCase()) {
            case "login":
                handleLoginCommand(params);
                break;
            case "register":
                handleRegisterCommand(params);
                break;
            case "logout":
                handleUserLogout();
                view.showLoginForm();
                break;
            case "list":
                loadAllInfluencers();
                break;
            case "search":
                String query = (String) params.get("query");
                handleInfluencerSearch(query != null ? query : "");
                break;
            case "sort":
                handleSortCommand(params);
                break;
            case "filter":
                handleInfluencerFilter(params);
                break;
            case "addtofavorites":
                if (params.containsKey("influencer")) {
                    handleAddToFavorites((Influencer) params.get("influencer"));
                } else {
                    view.showError("Missing influencer object.");
                }
                break;
            case "showfavorites":
                showUserFavoritesView();
                break;
            case "subscribe":
                handleSubscription();
                break;
            case "unsubscribe":
                handleUnsubscription();
                break;
            case "export":
                handleExportCommand(params);
                break;
            case "import":
                handleImportCommand(params);
                break;
            default:
                view.showError("Unknown action: " + action);
                break;
        }
    }

    private void handleLoginCommand(Map<String, Object> params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        User loggedInUser = handleUserLogin(username, password);
        if (loggedInUser != null) {
            view.showUserProfile(loggedInUser);
        } else {
            view.showError("Invalid username or password.");
            view.showLoginForm();
        }
    }

    private void handleRegisterCommand(Map<String, Object> params) {
        User user = (User) params.get("user");
        try {
            handleUserRegistration(user);
            view.showLoginForm();
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
            view.showRegistrationForm();
        }
    }

    private void handleSortCommand(Map<String, Object> params) {
        validateUser();
        String criteria = (String) params.get("criteria");
        boolean ascending = Boolean.TRUE.equals(params.get("ascending"));
        handleInfluencerSort(criteria, ascending);
    }

    private void handleExportCommand(Map<String, Object> params) {
        validateUser();
        String format = (String) params.get("format");
        String path = (String) params.get("path");
        List<Influencer> data = (List<Influencer>) params.get("data");
        if (format == null || path == null || data == null) {
            view.showExportError("Missing parameters for export (format/path/data).");
            return;
        }
        exporter = getExporterForFormat(format);
        if (exporter == null) {
            view.showExportError("Unsupported export format: " + format);
            return;
        }
        boolean success = exporter.export(data, path);
        if (success) {
            view.showExportSuccess(path);
        } else {
            view.showExportError("Failed to export data.");
        }
    }

    private void handleImportCommand(Map<String, Object> params) {
        validateUser();
        String format = (String) params.get("format");
        String path = (String) params.get("path");
        if (format == null || path == null) {
            view.showError("Missing parameters for import (format/path).");
            return;
        }
        importer = getImporterForFormat(format);
        if (importer == null) {
            view.showError("Unsupported import format: " + format);
            return;
        }
        List<Influencer> importedData = importer.importData(path);
        if (importedData.isEmpty()) {
            view.showError("Failed to import data or file was empty.");
        } else {
            for (Influencer influencer : importedData) {
                repository.save(influencer);
            }
            currentWorkingSet = new ArrayList<>(importedData);
            view.showImportSuccess(importedData.size() + " influencers imported.");
            loadAllInfluencers();
        }
    }

    @Override
    public String getControllerName() {
        return controllerName;
    }

    @Override
    public void handleUserRegistration(User user) {
        userManager.registerUser(user);
    }

    @Override
    public User handleUserLogin(String username, String password) {
        User user = userManager.authenticateUser(username, password);
        if (user != null) {
            setCurrentUser(user);
        }
        return user;
    }

    @Override
    public void handleUserLogout() {
        setCurrentUser(null);
        view.showLoginForm();
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
        view.displaySearchResults(results);
    }

    // New method: loadAllInfluencers
    @Override
    public void loadAllInfluencers() {
        validateUser();
        List<Influencer> allInfluencers = repository.findAll();
        currentWorkingSet = new ArrayList<>(allInfluencers);
        view.displayInfluencers(allInfluencers);
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        view.setCurrentUser(user);
        if (user != null) {
            this.userFavorites = new UserFavorites(user);
        } else {
            this.userFavorites = null;
        }
    }

    public void handleInfluencerFilter(Map<String, Object> filterCriteria) {
        validateUser();
        List<Influencer> results = new ArrayList<>(currentWorkingSet);
        if (filterCriteria.containsKey("platform")) {
            String platform = (String) filterCriteria.get("platform");
            results = filterByPlatform(results, platform);
        } else if (filterCriteria.containsKey("category")) {
            String category = (String) filterCriteria.get("category");
            results = filterByCategory(results, category);
        } else if (filterCriteria.containsKey("minFollowers") || filterCriteria.containsKey("maxFollowers")) {
            int min = filterCriteria.containsKey("minFollowers") ? (int) filterCriteria.get("minFollowers") : 0;
            int max = filterCriteria.containsKey("maxFollowers") ? (int) filterCriteria.get("maxFollowers") : Integer.MAX_VALUE;
            results = filterByFollowerRange(results, min, max);
        } else if (filterCriteria.containsKey("country")) {
            String country = (String) filterCriteria.get("country");
            results = filterByCountry(results, country);
        }
        currentWorkingSet = results;
        view.displayInfluencers(results);
    }

    public void handleInfluencerSort(String criteria, boolean ascending) {
        validateUser();
        List<Influencer> results = new ArrayList<>(currentWorkingSet);
        switch (criteria != null ? criteria.toLowerCase() : "") {
            case "name":
                results.sort((a, b) -> {
                    int r = a.getName().compareToIgnoreCase(b.getName());
                    return ascending ? r : -r;
                });
                break;
            case "followers":
                results.sort((a, b) -> {
                    int r = Integer.compare(a.getFollowerCount(), b.getFollowerCount());
                    return ascending ? r : -r;
                });
                break;
            case "adrate":
                validateSubscription();
                results.sort((a, b) -> {
                    int r = Double.compare(a.getAdRate(), b.getAdRate());
                    return ascending ? r : -r;
                });
                break;
            default:
                return;
        }
        currentWorkingSet = results;
        view.displayInfluencers(results);
    }

    public void handleAddToFavorites(Influencer influencer) {
        validateUser();
        if (userFavorites == null) {
            userFavorites = new UserFavorites(currentUser);
        }
        userFavorites.addItem(influencer);
        loadAllFavorites();
    }

    public void handleRemoveFromFavorites(Influencer influencer) {
        validateUser();
        if (userFavorites != null) {
            userFavorites.removeItem(influencer);
            loadAllFavorites();
        }
    }

    public void loadAllFavorites() {
        validateUser();
        if (userFavorites != null) {
            view.displayFavorites(userFavorites.getAllItems());
        }
    }

    public void showUserFavoritesView() {
        validateUser();
        loadAllFavorites();
        view.showUserFavoritesView();
    }

    private void handleSubscription() {
        validateUser();
        currentUser.subscribe();
        view.showUserProfile(currentUser);
        System.out.println("You have successfully subscribed to Premium.");
    }

    private void handleUnsubscription() {
        validateUser();
        currentUser.unsubscribe();
        view.showUserProfile(currentUser);
        System.out.println("You have unsubscribed from Premium.");
    }

    private void validateUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }
    }

    private void validateSubscription() {
        validateUser();
        if (!currentUser.isSubscribed()) {
            throw new IllegalStateException("Premium subscription required for this operation.");
        }
    }

    private List<Influencer> filterByPlatform(List<Influencer> influencers, String platform) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer inf : influencers) {
            if (inf.getPlatform().equalsIgnoreCase(platform)) {
                results.add(inf);
            }
        }
        return results;
    }

    private List<Influencer> filterByCategory(List<Influencer> influencers, String category) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer inf : influencers) {
            if (inf.getCategory().equalsIgnoreCase(category)) {
                results.add(inf);
            }
        }
        return results;
    }

    private List<Influencer> filterByFollowerRange(List<Influencer> influencers, int min, int max) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer inf : influencers) {
            int count = inf.getFollowerCount();
            if (count >= min && count <= max) {
                results.add(inf);
            }
        }
        return results;
    }

    private List<Influencer> filterByCountry(List<Influencer> influencers, String country) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer inf : influencers) {
            if (inf.getCountry().equalsIgnoreCase(country)) {
                results.add(inf);
            }
        }
        return results;
    }

    private IExporter getExporterForFormat(String format) {
        if (format == null) return null;
        switch (format.toLowerCase()) {
            case "csv":
                return new CSVExporter();
            case "json":
                return new JSONExporter();
            default:
                return null;
        }
    }

    private IImporter getImporterForFormat(String format) {
        if (format == null) return null;
        switch (format.toLowerCase()) {
            case "csv":
                return new CSVImporter();
            case "json":
                return new JSONImporter();
            default:
                return null;
        }
    }

    // Load sample data if no file was loaded.
    private void loadSampleData() {
        repository.save(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        repository.save(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));
        repository.save(new Influencer("David Lee", "TikTok", "Comedy", 1500000, "Canada", 3000.0));
        repository.save(new Influencer("Sophia Chen", "Instagram", "Fashion", 800000, "China", 1800.0));
        repository.save(new Influencer("Michael Brown", "YouTube", "Gaming", 3000000, "USA", 7000.0));
        try {
            userManager.registerUser(new User("admin", "admin"));
            userManager.registerUser(new User("premium", "premium"));
            User premiumUser = userManager.findUser("premium");
            if (premiumUser != null) {
                premiumUser.subscribe();
            }
        } catch (IllegalArgumentException e) {
            // Ignore duplicate registrations.
        }
    }
}
