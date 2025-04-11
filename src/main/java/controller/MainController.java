package controller;

import model.*;
import view.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController implements IController {
    private String controllerName;
    private InfluencerRepository repository;
    private UserManager userManager;
    private UserFavorites userFavorites;
    private MainView mainView;
    private User currentUser;
    private List<Influencer> currentWorkingSet;

    public MainController(MainView mainView) {
        this.controllerName = "Main Controller";
        this.mainView = mainView;
        this.repository = new InfluencerRepository();
        this.userManager = new UserManager();
        this.currentWorkingSet = new ArrayList<>();
    }

    @Override
    public void initialize() {
        if (!tryLoadDataFromFile()) {
            loadSampleData();
        }
        mainView.setVisible(true);
    }

    private boolean tryLoadDataFromFile() {
        String defaultDataPath = "src/main/resources/data/influencers.csv";
        CSVImporter importer = new CSVImporter();
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
                boolean ascending = params.containsKey("ascending") ? (boolean) params.get("ascending") : false;
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

            default:
                mainView.showError("Unknown action: " + action);
                break;
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
        mainView.displayInfluencers(results);
    }

    private List<Influencer> filterByPlatform(List<Influencer> influencers, String platform) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : influencers) {
            if (influencer.getPlatform().equalsIgnoreCase(platform)) {
                results.add(influencer);
            }
        }
        return results;
    }

    private List<Influencer> filterByCategory(List<Influencer> influencers, String category) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : influencers) {
            if (influencer.getCategory().equalsIgnoreCase(category)) {
                results.add(influencer);
            }
        }
        return results;
    }

    private List<Influencer> filterByFollowerRange(List<Influencer> influencers, int min, int max) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : influencers) {
            int followers = influencer.getFollowerCount();
            if (followers >= min && followers <= max) {
                results.add(influencer);
            }
        }
        return results;
    }

    private List<Influencer> filterByCountry(List<Influencer> influencers, String country) {
        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : influencers) {
            if (influencer.getCountry().equalsIgnoreCase(country)) {
                results.add(influencer);
            }
        }
        return results;
    }

    @Override
    public void handleInfluencerSort(String sortCriteria, boolean ascending) {
        validateUser();
        List<Influencer> results = new ArrayList<>(currentWorkingSet);

        switch (sortCriteria) {
            case "name":
                results.sort((a, b) -> ascending ? a.getName().compareToIgnoreCase(b.getName()) : b.getName().compareToIgnoreCase(a.getName()));
                break;
            case "followers":
                results.sort((a, b) -> ascending ? Integer.compare(a.getFollowerCount(), b.getFollowerCount()) : Integer.compare(b.getFollowerCount(), a.getFollowerCount()));
                break;
            case "adRate":
                validateSubscription();
                results.sort((a, b) -> ascending ? Double.compare(a.getAdRate(), b.getAdRate()) : Double.compare(b.getAdRate(), a.getAdRate()));
                break;
            default:
                return;
        }

        currentWorkingSet = results;
        mainView.displayInfluencers(results);
    }

    @Override
    public void loadAllInfluencers() {
        validateUser();
        List<Influencer> allInfluencers = repository.findAll();
        currentWorkingSet = new ArrayList<>(allInfluencers);
        mainView.displayInfluencers(allInfluencers);
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

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        mainView.setCurrentUser(user);

        if (user != null) {
            this.userFavorites = new UserFavorites(user);
        } else {
            this.userFavorites = null;
        }
    }

    private void validateUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user logged in");
        }
    }

    private void validateSubscription() {
        validateUser();
        if (!currentUser.isSubscribed()) {
            throw new IllegalStateException("Premium subscription required");
        }
    }

    private void updateViews() {
        mainView.setCurrentUser(currentUser);
    }

    private void loadSampleData() {
        repository.save(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        repository.save(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));
        repository.save(new Influencer("David Lee", "TikTok", "Comedy", 1500000, "Canada", 3000.0));
        repository.save(new Influencer("Sophia Chen", "Instagram", "Fashion", 800000, "China", 1800.0));
        repository.save(new Influencer("Michael Brown", "YouTube", "Gaming", 3000000, "USA", 7000.0));
        repository.save(new Influencer("Olivia Garcia", "TikTok", "Dance", 1200000, "Spain", 2200.0));
        repository.save(new Influencer("William Kim", "Instagram", "Travel", 600000, "South Korea", 1500.0));
        repository.save(new Influencer("Emily Martinez", "YouTube", "Cooking", 900000, "Mexico", 2000.0));
        repository.save(new Influencer("James Wilson", "Twitch", "Gaming", 400000, "Australia", 1200.0));
        repository.save(new Influencer("Ava Taylor", "Instagram", "Lifestyle", 700000, "USA", 1800.0));

        try {
            userManager.registerUser(new User("admin", "admin"));
            userManager.registerUser(new User("premium", "premium"));
            User premiumUser = userManager.findUser("premium");
            premiumUser.subscribe();
        } catch (IllegalArgumentException ignored) {
        }
    }

    public MainView getMainView() {
        return mainView;
    }
}
