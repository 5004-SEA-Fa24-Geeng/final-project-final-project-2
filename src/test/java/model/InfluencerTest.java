package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Influencer class.
 */
public class InfluencerTest {
    private Influencer influencer;
    private static final String NAME = "John Doe";
    private static final String PLATFORM = "Instagram";
    private static final String CATEGORY = "Fitness";
    private static final int FOLLOWERS = 1000000;
    private static final String COUNTRY = "USA";
    private static final double AD_RATE = 2500.0;

    @BeforeEach
    void setUp() {
        influencer = new Influencer(NAME, PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(NAME, influencer.getName());
        assertEquals(PLATFORM, influencer.getPlatform());
        assertEquals(CATEGORY, influencer.getCategory());
        assertEquals(FOLLOWERS, influencer.getFollowers());
        assertEquals(COUNTRY, influencer.getCountry());
        assertEquals(AD_RATE, influencer.getAdRate());
    }

    @Test
    void testSetters() {
        String newName = "Jane Doe";
        String newPlatform = "YouTube";
        String newCategory = "Gaming";
        int newFollowers = 2000000;
        String newCountry = "UK";
        double newAdRate = 3500.0;

        influencer.setName(newName);
        influencer.setPlatform(newPlatform);
        influencer.setCategory(newCategory);
        influencer.setFollowers(newFollowers);
        influencer.setCountry(newCountry);
        influencer.setAdRate(newAdRate);

        assertEquals(newName, influencer.getName());
        assertEquals(newPlatform, influencer.getPlatform());
        assertEquals(newCategory, influencer.getCategory());
        assertEquals(newFollowers, influencer.getFollowers());
        assertEquals(newCountry, influencer.getCountry());
        assertEquals(newAdRate, influencer.getAdRate());
    }

    @Test
    void testEqualsAndHashCode() {
        Influencer sameInfluencer = new Influencer(NAME, PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);
        Influencer differentInfluencer = new Influencer("Different", PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);

        assertTrue(influencer.equals(sameInfluencer));
        assertEquals(influencer.hashCode(), sameInfluencer.hashCode());
        assertFalse(influencer.equals(differentInfluencer));
        assertNotEquals(influencer.hashCode(), differentInfluencer.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Influencer{name='" + NAME + "', platform='" + PLATFORM + 
                "', category='" + CATEGORY + "', followers=" + FOLLOWERS + 
                ", adRate=" + AD_RATE + ", country='" + COUNTRY + "'}";
        assertEquals(expected, influencer.toString());
    }

    @Test
    void testSetNegativeFollowers() {
        influencer.setFollowers(-1);
        assertEquals(-1, influencer.getFollowers());
    }

    @Test
    void testSetNegativeAdRate() {
        influencer.setAdRate(-1.0);
        assertEquals(-1.0, influencer.getAdRate());
    }

    @Test
    void testNullValues() {
        Influencer nullName = new Influencer(null, PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);
        assertNull(nullName.getName());
        
        Influencer nullPlatform = new Influencer(NAME, null, CATEGORY, FOLLOWERS, AD_RATE, COUNTRY);
        assertNull(nullPlatform.getPlatform());
        
        Influencer nullCategory = new Influencer(NAME, PLATFORM, null, FOLLOWERS, AD_RATE, COUNTRY);
        assertNull(nullCategory.getCategory());
        
        Influencer nullCountry = new Influencer(NAME, PLATFORM, CATEGORY, FOLLOWERS, AD_RATE, null);
        assertNull(nullCountry.getCountry());
    }
} 