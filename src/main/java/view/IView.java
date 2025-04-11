package view;

import model.Influencer;
import model.User;

import java.util.List;

public interface IView {

    void render();

    void update(Object data);

    void setVisible(boolean visible);

    void showInfluencerListView();

    void showUserFavoritesView();

    void showExportView();

    void showUserView();

    void showLoginForm();

    void showRegistrationForm();

    void showUserProfile(User user);

    void showError(String message);

    void setCurrentUser(User user);

    void displayInfluencers(List<Influencer> influencers);

    void displaySearchResults(List<Influencer> results);

    boolean shouldShowAdRate();

    void displayFavorites(List<Influencer> favorites);

    void showExportOptions();

    void showExportSuccess(String path);

    void showExportError(String error);

    List<Influencer> getCurrentInfluencers();

    List<Influencer> getCurrentFavorites();

    void showImportView();

    void showImportSuccess(String message);
}