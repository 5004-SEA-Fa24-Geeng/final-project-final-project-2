package model;


import java.util.Objects;

/**
 * Represents a user of the influencer management system.
 */
public class User {
    private String username;
    private String password;
    private boolean isSubscribed;

    /**
     * Constructs a User with the specified username and password.
     *
     * @param username the username for the user
     * @param password the password for the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isSubscribed = false; // Default value
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks if the user is subscribed to premium features.
     *
     * @return true if the user is subscribed, false otherwise
     */
    public boolean isSubscribed() {
        return isSubscribed;
    }

    /**
     * Subscribes the user to premium features.
     */
    public void subscribe() {
        this.isSubscribed = true;
    }

    /**
     * Unsubscribes the user from premium features.
     */
    public void unsubscribe() {
        this.isSubscribed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isSubscribed != user.isSubscribed) return false;
        if (!username.equals(user.username)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, isSubscribed);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isSubscribed=" + isSubscribed +
                '}';
    }

}
