package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager for user-related operations such as registration, authentication, and subscription.
 */
public class UserManager {

    private List<User> users;

    /**
     * Constructs a new UserManager with an empty list of users.
     */
    public UserManager() {
        this.users = new ArrayList<>();
    }

    // Registers a new user in the system.
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (findUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        users.add(user);

    }


    // Authenticates a user based on username and password.
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }


    // Finds a user by username.
    public User findUser(String username) {
        if (username == null) {
            return null;
        }

        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // Gets all subscribed users.
    public List<User> getSubscribedUsers() {
        return users.stream()
                .filter(User::isSubscribed)
                .collect(Collectors.toList());
    }


}
