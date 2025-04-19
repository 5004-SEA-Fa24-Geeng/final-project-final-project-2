package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an Influencer entity with relevant information for the influencer management system.
 */
public class Influencer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String platform;
    private String category;
    private int followers;
    private double adRate;
    private String country;

    /**
     * Constructs an Influencer with the specified attributes.
     *
     * @param name       the name of the influencer
     * @param platform   the social media platform they primarily use
     * @param category   the content category they specialize in
     * @param followers  the number of followers they have
     * @param adRate     the advertising rate they charge
     * @param country    the country they are based in
     */
    public Influencer(String name, String platform, String category, int followers, double adRate, String country) {
        this.name = name;
        this.platform = platform;
        this.category = category;
        this.followers = followers;
        this.adRate = adRate;
        this.country = country;
    }

    /**
     * Gets the name of the influencer.
     *
     * @return the name of the influencer
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the platform of the influencer.
     *
     * @return the platform of the influencer
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Gets the category of the influencer.
     *
     * @return the category of the influencer
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the follower count of the influencer.
     *
     * @return the follower count of the influencer
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * Gets the ad rate of the influencer.
     *
     * @return the ad rate of the influencer
     */
    public double getAdRate() {
        return adRate;
    }

    /**
     * Gets the country of the influencer.
     *
     * @return the country of the influencer
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the influencer.
     *
     * @param name the new name for the influencer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the platform of the influencer.
     *
     * @param platform the new platform for the influencer
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Sets the category of the influencer.
     *
     * @param category the new category for the influencer
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the follower count of the influencer.
     *
     * @param followers the new follower count for the influencer
     */
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    /**
     * Sets the ad rate of the influencer.
     *
     * @param adRate the new ad rate for the influencer
     */
    public void setAdRate(double adRate) {
        this.adRate = adRate;
    }

    /**
     * Sets the country of the influencer.
     *
     * @param country the new country for the influencer
     */
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Influencer that = (Influencer) o;
        return followers == that.followers &&
                Double.compare(that.adRate, adRate) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(category, that.category) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, platform, category, followers, adRate, country);
    }

    @Override
    public String toString() {
        return "Influencer{" +
                "name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", category='" + category + '\'' +
                ", followers=" + followers +
                ", adRate=" + adRate +
                ", country='" + country + '\'' +
                '}';
    }
}

