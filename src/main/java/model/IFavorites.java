package model;

import java.util.List;

public interface IFavorites {

    void addItem(Object item);

    void removeItem(Object item);

    List getAllItems();
}