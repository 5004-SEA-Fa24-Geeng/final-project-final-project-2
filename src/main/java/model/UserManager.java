package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manager for user-related operations such as registration, authentication, and subscription.
 */
public class UserManager {

    private List<User> users;
    private static final String USERS_FILE = "data/users.ser";

    /**
     * Constructs a new UserManager with an empty list of users.
     */
    public UserManager() {
        this.users = new ArrayList<>();
        loadUsers();
    }

    /**
     * Registers a new user in the system.
     *
     * @param user the user to register
     * @throws IllegalArgumentException if user is null or if username already exists
     */
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (findUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        users.add(user);
        saveUsers();
    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username the username to authenticate
     * @param password the password to verify
     * @return the User object if authentication is successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return the User object if found, null otherwise
     */
    public User findUser(String username) {
        if (username == null) {
            return null;
        }

        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all subscribed users.
     *
     * @return a list of all users with an active subscription
     */
    public List<User> getSubscribedUsers() {
        return users.stream()
                .filter(User::isSubscribed)
                .collect(Collectors.toList());
    }

    /**
     * Saves all users to a file.
     */
    private void saveUsers() {
        try {
            // Ensure directory exists
            Path dir = Paths.get(USERS_FILE).getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // Save to file
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
                out.writeObject(users);
            }
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    /**
     * Loads users from a file.
     * If the file doesn't exist or there's an error reading it, starts with an empty list.
     */
    private void loadUsers() {
        try {
            Path file = Paths.get(USERS_FILE);
            if (Files.exists(file)) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                    @SuppressWarnings("unchecked")
                    List<User> loadedUsers = (List<User>) in.readObject();
                    users = loadedUsers;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // If loading fails, start with empty list
            this.users = new ArrayList<>();
        }
    }
}
