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
    public List<Influencer> findAll() {
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
        return influencers.stream()
                .filter(i -> i.getPlatform().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByCategory(String category) {
        return influencers.stream()
                .filter(i -> i.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByFollowerRange(int min, int max) {
        return influencers.stream()
                .filter(i -> i.getFollowerCount() >= min && i.getFollowerCount() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Influencer> filterByCountry(String country) {
        return influencers.stream()
                .filter(i -> i.getCountry().equalsIgnoreCase(country))
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
