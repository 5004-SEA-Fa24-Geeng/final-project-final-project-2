package model;

import java.util.List;

/**
 * Interface defining operations for managing a collection of favorite items.
 */
public interface IFavorites {
    /**
     * Adds an item to the favorites collection.
     *
     * @param item the item to add
     */
    void addItem(Object item);
    
    /**
     * Removes an item from the favorites collection.
     *
     * @param item the item to remove
     */
    void removeItem(Object item);
    
    /**
     * Gets all items in the favorites collection.
     *
     * @return a list of all favorite items
     */
    List getAllItems();
} 