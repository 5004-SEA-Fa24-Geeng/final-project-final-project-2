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


}