package com.bdserver.impactassist.service;

import com.bdserver.impactassist.repo.AutoPartsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutoPartsServiceTest {

    @Mock
    private AutoPartsRepo autoPartsRepo;

    @InjectMocks
    private AutoPartsService autoPartsService;

    @Test
    void getAutoPartsAndServices() {
        when(autoPartsRepo.getAutoPartsAndServices(anyList(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(List.of());
        when(autoPartsRepo.getAutoPartsAndServicesCount(anyList(), anyString(), anyString())).thenReturn(0);
        Map<String, Object> result = autoPartsService.getAutoPartsAndServices(List.of(), "hello", 1, 5, "");
        assertEquals(0, result.get("total"));
        assertEquals(List.of(), result.get("autoParts"));
        assertEquals(1, result.get("currentPage"));
        assertEquals(0, result.get("totalPages"));
        assertNull(result.get("nextPage"));
    }

    @Test
    void getCategories() {
        when(autoPartsRepo.getCategories(anyString(), anyInt(), anyInt(), anyString())).thenReturn(List.of());
        when(autoPartsRepo.getCategoriesCount(anyString(), anyString())).thenReturn(0);
        Map<String, Object> result = autoPartsService.getCategories("hello", 1, 5, "");
        assertEquals(0, result.get("total"));
        assertEquals(List.of(), result.get("autoParts"));
        assertEquals(1, result.get("currentPage"));
        assertEquals(0, result.get("totalPages"));
        assertNull(result.get("nextPage"));
    }
}