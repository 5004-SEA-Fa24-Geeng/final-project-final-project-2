package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the Influencer class.
 */
public class InfluencerTest {
    private Influencer influencer;

    @BeforeEach
    public void setUp() {
        influencer = new Influencer("Test Influencer", "Instagram", "Fashion",
                1000000, "USA", 2500.0);
    }

    @Test
    public void testGetName() {
        assertEquals("Test Influencer", influencer.getName());
    }

    @Test
    public void testGetPlatform() {
        assertEquals("Instagram", influencer.getPlatform());
    }

    @Test
    public void testGetCategory() {
        assertEquals("Fashion", influencer.getCategory());
    }

    @Test
    public void testGetFollowerCount() {
        assertEquals(1000000, influencer.getFollowerCount());
    }

    @Test
    public void testGetCountry() {
        assertEquals("USA", influencer.getCountry());
    }

    @Test
    public void testGetAdRate() {
        assertEquals(2500.0, influencer.getAdRate(), 0.001);
    }

    @Test
    public void testSetName() {
        influencer.setName("New Name");
        assertEquals("New Name", influencer.getName());
    }

    @Test
    public void testSetPlatform() {
        influencer.setPlatform("YouTube");
        assertEquals("YouTube", influencer.getPlatform());
    }

    @Test
    public void testSetCategory() {
        influencer.setCategory("Fitness");
        assertEquals("Fitness", influencer.getCategory());
    }

    @Test
    public void testSetFollowerCount() {
        influencer.setFollowerCount(2000000);
        assertEquals(2000000, influencer.getFollowerCount());
    }

    @Test
    public void testSetCountry() {
        influencer.setCountry("UK");
        assertEquals("UK", influencer.getCountry());
    }

    @Test
    public void testSetAdRate() {
        influencer.setAdRate(3000.0);
        assertEquals(3000.0, influencer.getAdRate(), 0.001);
    }

    @Test
    public void testEquals() {
        Influencer sameInfluencer = new Influencer("Test Influencer", "Instagram", "Fashion",
                1000000, "USA", 2500.0);
        Influencer differentInfluencer = new Influencer("Different Influencer", "TikTok", "Comedy",
                500000, "Canada", 1500.0);

        assertEquals(influencer, sameInfluencer);
        assertNotEquals(influencer, differentInfluencer);
    }
}