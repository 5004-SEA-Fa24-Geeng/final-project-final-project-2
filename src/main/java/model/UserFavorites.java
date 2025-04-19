package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * UserFavorites class manages a user's favorite influencers.
 * Implements the IFavorites interface for Influencer objects.
 * Handles saving and loading favorites from a file.
 */
public class UserFavorites implements IFavorites<Influencer> {
    private String username;
    private List<Influencer> favorites;
    private static final String FAVORITES_DIR = "src/main/resources/data/favorites/";

    /**
     * Constructs a UserFavorites object for the specified user.
     *
     * @param user the user whose favorites will be managed
     * @throws IllegalArgumentException if user is null
     */
    public UserFavorites(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.username = user.getUsername();
        this.favorites = new ArrayList<>();
        loadFavorites();
    }

    @Override
    public void addItem(Influencer item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null influencer");
        }
        if (!contains(item)) {
            favorites.add(item);
            saveFavorites();
        }
    }

    @Override
    public void removeItem(Influencer item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot remove null influencer");
        }
        if (contains(item)) {
            favorites.removeIf(fav ->
                    fav.getName().equals(item.getName()) &&
                            fav.getPlatform().equals(item.getPlatform()) &&
                            fav.getCategory().equals(item.getCategory()) &&
                            fav.getFollowers() == item.getFollowers() &&
                            fav.getAdRate() == item.getAdRate() &&
                            fav.getCountry().equals(item.getCountry())
            );
            saveFavorites();
        }
    }

    @Override
    public List<Influencer> getAllItems() {
        return new ArrayList<>(favorites);
    }

    @Override
    public boolean contains(Influencer item) {
        if (item == null) {
            return false;
        }
        return favorites.stream().anyMatch(fav ->
                fav.getName().equals(item.getName()) &&
                        fav.getPlatform().equals(item.getPlatform()) &&
                        fav.getCategory().equals(item.getCategory()) &&
                        fav.getFollowers() == item.getFollowers() &&
                        fav.getAdRate() == item.getAdRate() &&
                        fav.getCountry().equals(item.getCountry())
        );
    }

    @Override
    public List<Influencer> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchTerm = name.toLowerCase().trim();
        List<Influencer> results = new ArrayList<>();
        for (Influencer influencer : favorites) {
            if (influencer.getName().toLowerCase().contains(searchTerm)) {
                results.add(influencer);
            }
        }
        return results;
    }

    /**
     * Loads favorites from a file.
     * The file is expected to be in the format: name,platform,category,followers,adRate,country
     */
    private void loadFavorites() {
        Path filePath = Paths.get(FAVORITES_DIR + username + ".txt");
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            String name = parts[0].trim();
                            String platform = parts[1].trim();
                            String category = parts[2].trim();
                            int followers = Integer.parseInt(parts[3].trim());
                            double adRate = Double.parseDouble(parts[4].trim());
                            String country = parts[5].trim();
                            Influencer influencer = new Influencer(name, platform, category, followers, adRate, country);
                            if (!favorites.contains(influencer)) {
                                favorites.add(influencer);
                            }
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading favorites: " + e.getMessage());
            }
        }
    }

    /**
     * Saves favorites to a file in the format: name,platform,category,followers,adRate,country
     */
    private void saveFavorites() {
        Path filePath = Paths.get(FAVORITES_DIR + username + ".txt");
        try {
            Files.createDirectories(filePath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Influencer influencer : favorites) {
                    writer.write(String.format("%s,%s,%s,%d,%.2f,%s%n",
                            influencer.getName(),
                            influencer.getPlatform(),
                            influencer.getCategory(),
                            influencer.getFollowers(),
                            influencer.getAdRate(),
                            influencer.getCountry()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving favorites: " + e.getMessage());
        }
    }

    /**
     * Gets the username of the user associated with these favorites.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}

