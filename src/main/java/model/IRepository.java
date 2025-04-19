package model;

import java.util.List;

/**
 * Interface for a generic repository that manages entities.
 * Combines functionality from ISearchable, ISortable, and IFilterable interfaces.
 * Provides operations for saving, deleting, and retrieving entities.
 *
 * @param <T> the type of entity managed by the repository
 */
public interface IRepository<T> extends ISortable<T>, IFilterable<T> {

    /**
     * Saves an entity to the repository.
     *
     * @param entity the entity to save
     */
    void save(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity the entity to delete
     */
    void delete(T entity);

    /**
     * Returns all entities in the repository.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Searches for entities by name.
     *
     * @param name the name to search for
     * @return a list of entities matching the name
     */
    List<T> searchByName(String name);

    /**
     * Filters entities by platform.
     *
     * @param platform the platform to filter by
     * @return a list of entities matching the platform
     */
    List<T> filterByPlatform(String platform);

    /**
     * Filters entities by category.
     *
     * @param category the category to filter by
     * @return a list of entities matching the category
     */
    List<T> filterByCategory(String category);

    /**
     * Filters entities by follower count range.
     *
     * @param min the minimum number of followers
     * @param max the maximum number of followers
     * @return a list of entities with follower counts in the specified range
     */
    List<T> filterByFollowerRange(int min, int max);

    /**
     * Filters entities by country.
     *
     * @param country the country to filter by
     * @return a list of entities matching the country
     */
    List<T> filterByCountry(String country);

    /**
     * Sorts entities by name.
     *
     * @return a list of entities sorted by name
     */
    List<T> sortByName();

    /**
     * Sorts entities by follower count.
     *
     * @return a list of entities sorted by follower count
     */
    List<T> sortByFollowers();

    /**
     * Sorts entities by advertisement rate.
     *
     * @return a list of entities sorted by advertisement rate
     */
    List<T> sortByAdRate();
}