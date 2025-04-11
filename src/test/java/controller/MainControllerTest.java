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
        
        // 使用反射设置currentWorkingSet
        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentWorkingSet");
            field.setAccessible(true);
            field.set(controller, new ArrayList<>(testInfluencers));
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        
        // 使用反射设置currentUser
        try {
            User testUser = new User("testUser", "password");
            testUser.subscribe(); // 确保用户有订阅，可以进行所有操作
            
            java.lang.reflect.Field field = MainController.class.getDeclaredField("currentUser");
            field.setAccessible(true);
            field.set(controller, testUser);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    public void testResetWorkingSet() {
        // 设置一个模拟的repository
        InfluencerRepository mockRepo = mock(InfluencerRepository.class);
        when(mockRepo.findAll()).thenReturn(testInfluencers);
        
        try {
            java.lang.reflect.Field field = MainController.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(controller, mockRepo);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        
        // 调用resetWorkingSet方法
        controller.resetWorkingSet();
        
        // 验证view.displayInfluencers被调用
        verify(mockView).displayInfluencers(testInfluencers);
        
        // 验证currentWorkingSet被重置
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
        // 首先搜索名字包含"John"的influencer
        controller.handleInfluencerSearch("John");
        
        // 捕获传递给view的结果
        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displaySearchResults(captor.capture());
        
        List<Influencer> firstResults = captor.getValue();
        assertEquals(2, firstResults.size());
        assertEquals("John Smith", firstResults.get(0).getName());
        
        // 确认currentWorkingSet更新为第一次搜索的结果
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
        // 创建过滤条件：平台为"Instagram"
        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("platform", "Instagram");
        
        // 应用过滤
        controller.handleInfluencerFilter(filterParams);
        
        // 捕获传递给view的结果
        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> filteredResults = captor.getValue();
        assertEquals(2, filteredResults.size());
        
        // 验证过滤结果都是Instagram平台的
        for (Influencer inf : filteredResults) {
            assertEquals("Instagram", inf.getPlatform());
        }
        
        // 确认currentWorkingSet更新为过滤结果
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
        
        // 重置mock以继续测试
        reset(mockView);
        
        // 在Instagram结果上继续过滤：国家为"USA"
        filterParams.clear();
        filterParams.put("country", "USA");
        
        controller.handleInfluencerFilter(filterParams);
        
        // 再次捕获结果
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> secondFilterResults = captor.getValue();
        assertEquals(1, secondFilterResults.size());
        assertEquals("John Smith", secondFilterResults.get(0).getName());
        assertEquals("Instagram", secondFilterResults.get(0).getPlatform());
        assertEquals("USA", secondFilterResults.get(0).getCountry());
    }
    
    @Test
    public void testContinuousSorting() {
        // 首先按名称排序
        controller.handleInfluencerSort("name", true);
        
        // 捕获传递给view的结果
        ArgumentCaptor<List<Influencer>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> sortedByName = captor.getValue();
        assertEquals(5, sortedByName.size());
        assertEquals("David Lee", sortedByName.get(0).getName());
        assertEquals("Emma Johnson", sortedByName.get(1).getName());
        
        // 重置mock以继续测试
        reset(mockView);
        
        // 在排序结果的基础上进行过滤：国家为"USA"
        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("country", "USA");
        
        controller.handleInfluencerFilter(filterParams);
        
        // 再次捕获结果
        verify(mockView).displayInfluencers(captor.capture());
        
        List<Influencer> filteredResults = captor.getValue();
        assertEquals(2, filteredResults.size());
        // 验证结果都来自USA
        for (Influencer inf : filteredResults) {
            assertEquals("USA", inf.getCountry());
        }
    }
    
    @Test
    public void testImportUpdatesWorkingSet() {
        // 创建模拟的导入器和导入数据
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
            
            // 设置一个模拟的repository
            InfluencerRepository mockRepo = mock(InfluencerRepository.class);
            field = MainController.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(controller, mockRepo);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        
        // 调用handleImport
        controller.handleImport("csv", "test.csv");
        
        // 验证currentWorkingSet被更新为导入的数据
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