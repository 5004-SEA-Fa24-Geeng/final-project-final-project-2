package controller;

import Controller.MainController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import view.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the MainController class, focusing on the continuous operations.
 */
public class MainControllerTest {
    private MainController controller;
    
    @Mock
    private MainView mockView;
    
    private List<Influencer> testInfluencers;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        controller = new MainController(mockView);
        
        // Load sample data for testing
        testInfluencers = new ArrayList<>();
        testInfluencers.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        testInfluencers.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));
        testInfluencers.add(new Influencer("David Lee", "TikTok", "Comedy", 1500000, "Canada", 3000.0));
        testInfluencers.add(new Influencer("Sophia Chen", "Instagram", "Fashion", 800000, "China", 1800.0));
        testInfluencers.add(new Influencer("Michael Brown", "YouTube", "Gaming", 3000000, "USA", 7000.0));
        

        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            field.set(controller, new ArrayList<>(testInfluencers));
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        

        try {
            User testUser = new User("testUser", "password");
            testUser.subscribe();
            
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentUser");
            field.setAccessible(true);
            field.set(controller, testUser);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    public void testResetWorkingSet() {

        InfluencerRepository mockRepo = mock(InfluencerRepository.class);
        when(mockRepo.findAll()).thenReturn(testInfluencers);
        
        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(controller, mockRepo);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        

        controller.resetWorkingSet();
        

        verify(mockView).displayInfluencers(testInfluencers);
        

        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            List<Influencer> currentWorkingSet = (List<Influencer>) field.get(controller);
            
            assertEquals(testInfluencers.size(), currentWorkingSet.size());
            for (int i = 0; i < testInfluencers.size(); i++) {
                assertEquals(testInfluencers.get(i), currentWorkingSet.get(i));
            }
        } catch (Exception e) {
            fail("Failed to verify test: " + e.getMessage());
        }
    }
    
    @Test
    public void testContinuousSearching() {

        controller.handleInfluencerSearch("John");
        

        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displaySearchResults(captor.capture());
        
        List<Influencer> firstResults = captor.getValue();
        assertEquals(2, firstResults.size());
        assertEquals("John Smith", firstResults.get(0).getName());
        

        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            List<Influencer> currentWorkingSet = (List<Influencer>) field.get(controller);
            
            assertEquals(2, currentWorkingSet.size());
            assertEquals("John Smith", currentWorkingSet.get(0).getName());
        } catch (Exception e) {
            fail("Failed to verify test: " + e.getMessage());
        }
    }
    
    @Test
    public void testContinuousFiltering() {

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("platform", "Instagram");
        

        controller.handleInfluencerFilter(filterParams);
        

        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> filteredResults = captor.getValue();
        assertEquals(2, filteredResults.size());
        

        for (Influencer inf : filteredResults) {
            assertEquals("Instagram", inf.getPlatform());
        }
        

        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            List<Influencer> currentWorkingSet = (List<Influencer>) field.get(controller);
            
            assertEquals(2, currentWorkingSet.size());
            for (Influencer inf : currentWorkingSet) {
                assertEquals("Instagram", inf.getPlatform());
            }
        } catch (Exception e) {
            fail("Failed to verify test: " + e.getMessage());
        }
        

        reset(mockView);
        

        filterParams.clear();
        filterParams.put("country", "USA");
        
        controller.handleInfluencerFilter(filterParams);
        

        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> secondFilterResults = captor.getValue();
        assertEquals(1, secondFilterResults.size());
        assertEquals("John Smith", secondFilterResults.get(0).getName());
        assertEquals("Instagram", secondFilterResults.get(0).getPlatform());
        assertEquals("USA", secondFilterResults.get(0).getCountry());
    }
    
    @Test
    public void testContinuousSorting() {

        controller.handleInfluencerSort("name", true);
        

        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> sortedByName = captor.getValue();
        assertEquals(5, sortedByName.size());
        assertEquals("David Lee", sortedByName.get(0).getName());
        assertEquals("Emma Johnson", sortedByName.get(1).getName());
        

        reset(mockView);
        

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("country", "USA");
        
        controller.handleInfluencerFilter(filterParams);
        

        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> filteredResults = captor.getValue();
        assertEquals(2, filteredResults.size());

        for (Influencer inf : filteredResults) {
            assertEquals("USA", inf.getCountry());
        }
    }
    
    @Test
    public void testImportUpdatesWorkingSet() {

        IImporter mockImporter = mock(IImporter.class);
        List<Influencer> importedData = new ArrayList<>();
        importedData.add(new Influencer("John Smith", "Instagram", "Fitness", 500000, "USA", 2500.0));
        importedData.add(new Influencer("Emma Johnson", "YouTube", "Beauty", 2000000, "UK", 5000.0));
        importedData.add(new Influencer("David Lee", "TikTok", "Comedy", 1500000, "Canada", 3000.0));
        importedData.add(new Influencer("Sophia Chen", "Instagram", "Fashion", 800000, "China", 1800.0));
        importedData.add(new Influencer("Michael Brown", "YouTube", "Gaming", 3000000, "USA", 7000.0));

        when(mockImporter.importData(anyString())).thenReturn(importedData);
        
        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("importer");
            field.setAccessible(true);
            field.set(controller, mockImporter);
            

            InfluencerRepository mockRepo = mock(InfluencerRepository.class);
            field = MainController.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(controller, mockRepo);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        

        controller.handleImport("csv", "test.csv");
        

        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            List<Influencer> currentWorkingSet = (List<Influencer>) field.get(controller);
            
            assertEquals(importedData.size(), currentWorkingSet.size());
            assertEquals(importedData.get(0), currentWorkingSet.get(0));
        } catch (Exception e) {
            fail("Failed to verify test: " + e.getMessage());
        }
    }
} 