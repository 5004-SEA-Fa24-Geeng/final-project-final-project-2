package model;

import java.util.ArrayList;
import java.util.List;


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

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public List findAll() {

    }

    @Override
    public List<Influencer> searchByName(String name) {

    }

    @Override
    public List<Influencer> filterByPlatform(String platform) {

    }

    @Override
    public List<Influencer> filterByCategory(String category) {

    }

    @Override
    public List<Influencer> filterByFollowerRange(int min, int max) {

    }

    @Override
    public List<Influencer> filterByCountry(String country) {

    }

    @Override
    public List<Influencer> sortByName() {

    }

    @Override
    public List<Influencer> sortByFollowers() {

    }

    @Override
    public List<Influencer> sortByAdRate() {

    }
}
