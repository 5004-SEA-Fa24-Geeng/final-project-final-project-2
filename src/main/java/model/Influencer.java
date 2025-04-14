package model;

/**
 * Represents an Influencer entity with relevant information for the influencer management system.
 */
public class Influencer {
    private String name;
    private String platform;
    private String category;
    private int followerCount;
    private String country;
    private double adRate;
    
    /**
     * Constructs an Influencer with the specified attributes.
     *
     * @param name          the name of the influencer
     * @param platform      the social media platform they primarily use
     * @param category      the content category they specialize in
     * @param followerCount the number of followers they have
     * @param country       the country they are based in
     * @param adRate        the advertising rate they charge
     */
    public Influencer(String name, String platform, String category, 
                    int followerCount, String country, double adRate) {
        this.name = name;
        this.platform = platform;
        this.category = category;
        this.followerCount = followerCount;
        this.country = country;
        this.adRate = adRate;
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
    public int getFollowerCount() {
        return followerCount;
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
     * Gets the ad rate of the influencer.
     *
     * @return the ad rate of the influencer
     */
    public double getAdRate() {
        return adRate;
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
     * @param followerCount the new follower count for the influencer
     */
    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
    
    /**
     * Sets the country of the influencer.
     *
     * @param country the new country for the influencer
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Sets the ad rate of the influencer.
     *
     * @param adRate the new ad rate for the influencer
     */
    public void setAdRate(double adRate) {
        this.adRate = adRate;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Influencer that = (Influencer) o;
        
        if (followerCount != that.followerCount) return false;
        if (Double.compare(that.adRate, adRate) != 0) return false;
        if (!name.equals(that.name)) return false;
        if (!platform.equals(that.platform)) return false;
        if (!category.equals(that.category)) return false;
        return country.equals(that.country);
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + platform.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + followerCount;
        result = 31 * result + country.hashCode();
        temp = Double.doubleToLongBits(adRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    @Override
    public String toString() {
        return "Influencer{" +
                "name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", category='" + category + '\'' +
                ", followerCount=" + followerCount +
                ", country='" + country + '\'' +
                ", adRate=" + adRate +
                '}';
    }
} 