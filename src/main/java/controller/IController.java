package controller;

import model.User;
import model.Influencer;
import java.util.Map;

public interface IController {
    void initialize();
    void handleRequest(String action, Map<String, Object> params);
    String getControllerName();

    void handleUserRegistration(User user);
    User handleUserLogin(String username, String password);
    void handleUserLogout();
    void handleInfluencerSearch(String query);
    void loadAllInfluencers();
    void setCurrentUser(User user);
}
