package model;

import java.util.List;

/**
 * Interface for objects that support search operations.
 * Provides methods to search entities by name.
 *
 * @param <T> the type of entity to be searched
 */
public interface ISearchable<T> {
    /**
     * Searches for entities by name.
     *
     * @param name the name to search for
     * @return a list of entities matching the name
     */
    List<T> searchByName(String name);
}