package model;

import java.util.List;

public class UserFavorites implements IFavorites{
    private User user;
    private List<Influencer> favorites;


    @Override
    public void addItem(Object item) {

    }

    @Override
    public void removeItem(Object item) {

    }

    @Override
    public List getAllItems() {
        return List.of();
    }


    public List<Influencer> searchByName(String name) {

    }

    public User getUser() {
        return user;
    }


}
