package model;

import java.util.List;

/**
 * Interface defining operations for a repository of entities.
 */
public interface IRepository {
    /**
     * Saves an entity to the repository.
     *
     * @param entity the entity to save
     */
    void save(Object entity);
    
    /**
     * Deletes an entity from the repository.
     *
     * @param entity the entity to delete
     */
    void delete(Object entity);
    
    /**
     * Finds all entities in the repository.
     *
     * @return a list of all entities
     */
    List findAll();
    
    /**
     * Searches for influencers by name.
     *
     * @param name the name to search for
     * @return a list of matching influencers
     */
    List<Influencer> searchByName(String name);
    
    /**
     * Filters influencers by platform.
     *
     * @param platform the platform to filter by
     * @return a list of influencers on the specified platform
     */
    List<Influencer> filterByPlatform(String platform);
    
    /**
     * Filters influencers by category.
     *
     * @param category the category to filter by
     * @return a list of influencers in the specified category
     */
    List<Influencer> filterByCategory(String category);
    
    /**
     * Filters influencers by follower count range.
     *
     * @param min the minimum follower count
     * @param max the maximum follower count
     * @return a list of influencers within the specified follower count range
     */
    List<Influencer> filterByFollowerRange(int min, int max);
    
    /**
     * Filters influencers by country.
     *
     * @param country the country to filter by
     * @return a list of influencers from the specified country
     */
    List<Influencer> filterByCountry(String country);
    
    /**
     * Sorts influencers by name.
     *
     * @return a list of influencers sorted by name
     */
    List<Influencer> sortByName();
    
    /**
     * Sorts influencers by follower count.
     *
     * @return a list of influencers sorted by follower count
     */
    List<Influencer> sortByFollowers();
    
    /**
     * Sorts influencers by ad rate.
     *
     * @return a list of influencers sorted by ad rate
     */
    List<Influencer> sortByAdRate();
} 