package controller;

import model.Influencer;
import model.User;
import view.MainView;

import java.util.List;
import java.util.Map;

public interface IController {

    void initialize();

    void handleRequest(String action, Map<String, Object> params);

    String getControllerName();

    void handleUserRegistration(User user);

    User handleUserLogin(String username, String password);

    void handleUserLogout();

    void handleSubscription();

    void handleUnsubscription();

    void handleInfluencerSearch(String query);

    void handleInfluencerFilter(Map<String, Object> filterCriteria);

    void handleInfluencerSort(String sortCriteria, boolean ascending);

    void loadAllInfluencers();

    void handleAddToFavorites(Influencer influencer);

    void handleRemoveFromFavorites(Influencer influencer);

    void loadAllFavorites();

    void handleFavoritesSearch(String name);

    void handleExport(String format, String path, List<Influencer> data);

    void showInfluencerListView();

    void showUserFavoritesView();

    void showExportView();

    void showUserView();

    void setCurrentUser(User user);

    void showImportView();

    void handleImport(String format, String path);

    void resetWorkingSet();

    MainView getMainView();

}
