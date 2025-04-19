package model;

import java.util.List;

/**
 * Interface for objects that support sorting operations.
 * Provides methods to sort entities by different criteria.
 *
 * @param <T> the type of entity to be sorted
 */
public interface ISortable<T> {
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