package model;

import java.util.List;

/**
 * Interface for objects that support filtering operations.
 * Provides methods to filter entities by various criteria.
 *
 * @param <T> the type of entity to be filtered
 */
public interface IFilterable<T> {
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
}