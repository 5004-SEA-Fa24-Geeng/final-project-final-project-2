package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserFavorites implements IFavorites{
    private User user;
    private List<Influencer> favorites;

    // Constructs a new UserFavorites for the specified user.
    public UserFavorites(User user) {
        this.user = user;
        this.favorites = new ArrayList<>();
    }

    @Override
    public void addItem(Object item) {
        if (!(item instanceof Influencer)) {
            throw new IllegalArgumentException("Item must be an Influencer");
        }

        Influencer influencer = (Influencer) item;

        // Avoid duplicates
        if (!favorites.contains(influencer)) {
            favorites.add(influencer);
        }
    }

    @Override
    public void removeItem(Object item) {
        if (!(item instanceof Influencer)) {
            throw new IllegalArgumentException("Item must be an Influencer");
        }

        Influencer influencer = (Influencer) item;
        favorites.remove(influencer);
    }

    @Override
    public List getAllItems() {
            return new ArrayList<>(favorites);
    }


    // Searches for influencers by name in the favorites collection.
    public List<Influencer> searchByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>(favorites);
        }

        String searchName = name.toLowerCase();
        return favorites.stream()
                .filter(i -> i.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    // Gets the user associated with this favorites collection.
    public User getUser() {
        return user;
    }


}
