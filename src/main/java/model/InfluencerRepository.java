package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Repository implementation for managing Influencer entities.
 */
public class InfluencerRepository implements IRepository {
    private List<Influencer> influencers;

    /**
     * Constructs a new InfluencerRepository with an empty list of influencers.
     */
    public InfluencerRepository() {
        this.influencers = new ArrayList<>();
    }


    @Override
    public void save(Object entity) {
        if (!(entity instanceof Influencer)) {
            throw new IllegalArgumentException("Entity must be an Influencer");
        }

        Influencer influencer = (Influencer) entity;

        // Remove existing influencer with same name if exists
        influencers.removeIf(i -> i.getName().equals(influencer.getName()));

        // Add the new/updated influencer
        influencers.add(influencer);
    }

    @Override
    public void delete(Object entity) {
        if (!(entity instanceof Influencer)) {
            throw new IllegalArgumentException("Entity must be an Influencer");
        }

        Influencer influencer = (Influencer) entity;
        influencers.remove(influencer);
    }

    @Override
    public List findAll() {
        return new ArrayList<>(influencers);
    }

    @Override
    public List<Influencer> searchByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>(influencers);
        }

        String searchName = name.toLowerCase();
        return influencers.stream()
                .filter(i -> i.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByPlatform(String platform) {
        if (platform == null || platform.isEmpty()) {
            return new ArrayList<>(influencers);
        }

        String searchPlatform = platform.toLowerCase();
        return influencers.stream()
                .filter(i -> i.getPlatform().toLowerCase().equals(searchPlatform))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return new ArrayList<>(influencers);
        }

        String searchCategory = category.toLowerCase();
        return influencers.stream()
                .filter(i -> i.getCategory().toLowerCase().equals(searchCategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByFollowerRange(int min, int max) {
        if (min < 0) {
            min = 0;
        }

        final int finalMin = min;
        final int finalMax = max;

        return influencers.stream()
                .filter(i -> i.getFollowerCount() >= finalMin &&
                        (finalMax <= 0 || i.getFollowerCount() <= finalMax))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByCountry(String country) {
        if (country == null || country.isEmpty()) {
            return new ArrayList<>(influencers);
        }

        String searchCountry = country.toLowerCase();
        return influencers.stream()
                .filter(i -> i.getCountry().toLowerCase().equals(searchCountry))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> sortByName() {
        return influencers.stream()
                .sorted(Comparator.comparing(Influencer::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> sortByFollowers() {
        return influencers.stream()
                .sorted(Comparator.comparingInt(Influencer::getFollowerCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> sortByAdRate() {
        return influencers.stream()
                .sorted(Comparator.comparingDouble(Influencer::getAdRate).reversed())
                .collect(Collectors.toList());
    }
}
