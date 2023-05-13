package ntnu.idatt2106.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import ntnu.idatt2106.backend.service.GroceryHistoryService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class GroceryHistoryControllerTest {
    private GroceryHistoryController groceryHistoryController;

    @Mock
    private GroceryHistoryService groceryHistoryService;

    @Mock
    private RefrigeratorService refrigeratorService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        groceryHistoryController = new GroceryHistoryController(groceryHistoryService, refrigeratorService);
    }

    @Test
    public void testGetLastYearGroceryStats() throws RefrigeratorNotFoundException {
        long refrigeratorId = 1L;
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(refrigeratorId);

        GroceryStatisticDTO statisticDTO = new GroceryStatisticDTO();
        List<GroceryStatisticDTO> stats = Collections.singletonList(statisticDTO);

        when(refrigeratorService.getRefrigerator(anyLong())).thenReturn(refrigerator);
        when(groceryHistoryService.getStatsforLastYear(anyLong())).thenReturn(stats);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("GET");

        ResponseEntity<?> response = groceryHistoryController.getLastYearGroceryStats(refrigeratorId, request);

        assertEquals(stats, response.getBody());
    }
}
