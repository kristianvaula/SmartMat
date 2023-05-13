package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.model.GroceryHistory;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import ntnu.idatt2106.backend.repository.GroceryHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GroceryHistoryServiceTest {

    private GroceryHistoryService groceryHistoryService;

    @Mock
    private GroceryHistoryRepository groceryHistoryRepository;

    private final long refrigeratorId = 1L;
    private final Refrigerator refrigerator = Refrigerator.builder().name("test").address("test").id(1L).build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groceryHistoryService = new GroceryHistoryService(groceryHistoryRepository);
    }

    @Test
    void findGroceriesForMonth_returnsCorrectList() {
        // Setup
        int x = 2;
        List<GroceryHistory> groceryHistories = new ArrayList<>();
        groceryHistories.add(new GroceryHistory(1L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(2L, LocalDate.now(), refrigerator, true, 100));
        when(groceryHistoryRepository.findByDateConsumedBetweenAndRefrigeratorId(any(), any(), anyLong()))
                .thenReturn(groceryHistories);

        // Run
        List<GroceryHistory> result = groceryHistoryService.findGroceriesForMonth(0, refrigeratorId);

        // Verify
        assertEquals(groceryHistories, result);
        verify(groceryHistoryRepository, times(1))
                .findByDateConsumedBetweenAndRefrigeratorId(any(), any(), anyLong());
    }

    @Test
    void getStatsforLastYear_returnsCorrectList() {
        // Setup
        List<GroceryHistory> groceryHistories = new ArrayList<>();
        groceryHistories.add(new GroceryHistory(1L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(2L, LocalDate.now().minusMonths(3), refrigerator, false, 200));
        when(groceryHistoryRepository.findByDateConsumedBetweenAndRefrigeratorId(any(), any(), anyLong()))
                .thenReturn(groceryHistories);

        // Run
        List<GroceryStatisticDTO> result = groceryHistoryService.getStatsforLastYear(refrigeratorId);

        // Verify
        assertEquals(12, result.size());
        verify(groceryHistoryRepository, times(12))
                .findByDateConsumedBetweenAndRefrigeratorId(any(), any(), anyLong());
    }

    @Test
    void sumTrash_returnsCorrectValue() {
        // Setup
        List<GroceryHistory> groceryHistories = new ArrayList<>();
        groceryHistories.add(new GroceryHistory(1L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(2L, LocalDate.now(), refrigerator, false, 200));
        groceryHistories.add(new GroceryHistory(3L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(4L, LocalDate.now(), refrigerator, false, 200));

        // Run
        int result = groceryHistoryService.sumTrash(groceryHistories);

        // Verify
        assertEquals(200, result);
    }

    @Test
    void sumEaten_returnsCorrectValue() {
        // Setup
        List<GroceryHistory> groceryHistories = new ArrayList<>();
        groceryHistories.add(new GroceryHistory(1L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(2L, LocalDate.now(), refrigerator, false, 200));
        groceryHistories.add(new GroceryHistory(3L, LocalDate.now(), refrigerator, true, 100));
        groceryHistories.add(new GroceryHistory(4L, LocalDate.now(), refrigerator, false, 200));

        // Run
        int result = groceryHistoryService.sumEaten(groceryHistories);

        // Verify
        assertEquals(400, result);
    }

}
