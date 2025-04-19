package model;

import java.util.List;

/**
 * Interface for managing a collection of favorite items.
 * Provides functionality for adding, removing, retrieving, and searching favorite items.
 *
 * @param <T> the type of items in the favorites collection
 */
public interface IFavorites<T> {

    /**
     * Adds an item to the favorites collection.
     *
     * @param item the item to add
     */
    void addItem(T item);

    /**
     * Removes an item from the favorites collection.
     *
     * @param item the item to remove
     */
    void removeItem(T item);

    /**
     * Gets all items in the favorites collection.
     *
     * @return a list of all favorite items
     */
    List<T> getAllItems();

    /**
     * Checks if an item is in the favorites collection.
     *
     * @param item the item to check
     * @return true if the item is in the favorites, false otherwise
     */
    boolean contains(T item);

    /**
     * Searches for items in the favorites collection by name.
     *
     * @param name the name to search for
     * @return a list of items matching the search criteria
     */
    List<T> searchByName(String name);
}