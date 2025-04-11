package model;

import java.util.List;

public interface IRepository {

    void save(Object entity);

    void delete(Object entity);

    List findAll();

    List<Influencer> searchByName(String name);

    List<Influencer> filterByPlatform(String platform);

    List<Influencer> filterByCategory(String category);

    List<Influencer> filterByFollowerRange(int min, int max);

    List<Influencer> filterByCountry(String country);

    List<Influencer> sortByName();

    List<Influencer> sortByFollowers();

    List<Influencer> sortByAdRate();
}