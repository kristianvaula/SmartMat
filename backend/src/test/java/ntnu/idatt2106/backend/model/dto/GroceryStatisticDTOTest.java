package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroceryStatisticDTOTest {

    @Test
    void testNoArgsConstructor() {
        GroceryStatisticDTO groceryStatisticDTO = new GroceryStatisticDTO();
        assertNull(groceryStatisticDTO.getMonthName());
        assertNull(groceryStatisticDTO.getFoodEaten());
        assertNull(groceryStatisticDTO.getFoodWaste());
    }

    @Test
    void testAllArgsConstructor() {
        String monthName = "2023, January";
        Integer foodEaten = 50;
        Integer foodWaste = 20;

        GroceryStatisticDTO groceryStatisticDTO = new GroceryStatisticDTO(monthName, foodEaten, foodWaste);
        assertEquals(monthName, groceryStatisticDTO.getMonthName());
        assertEquals(foodEaten, groceryStatisticDTO.getFoodEaten());
        assertEquals(foodWaste, groceryStatisticDTO.getFoodWaste());
    }

    @Test
    void testBuilder() {
        String monthName = "2023, February";
        Integer foodEaten = 70;
        Integer foodWaste = 10;

        GroceryStatisticDTO groceryStatisticDTO = GroceryStatisticDTO.builder()
                .monthName(monthName)
                .foodEaten(foodEaten)
                .foodWaste(foodWaste)
                .build();
        assertEquals(monthName, groceryStatisticDTO.getMonthName());
        assertEquals(foodEaten, groceryStatisticDTO.getFoodEaten());
        assertEquals(foodWaste, groceryStatisticDTO.getFoodWaste());
    }

    @Test
    void testSettersAndGetters() {
        String monthName = "2023, March";
        Integer foodEaten = 80;
        Integer foodWaste = 5;

        GroceryStatisticDTO groceryStatisticDTO = new GroceryStatisticDTO();
        groceryStatisticDTO.setMonthName(monthName);
        groceryStatisticDTO.setFoodEaten(foodEaten);
        groceryStatisticDTO.setFoodWaste(foodWaste);

        assertEquals(monthName, groceryStatisticDTO.getMonthName());
        assertEquals(foodEaten, groceryStatisticDTO.getFoodEaten());
        assertEquals(foodWaste, groceryStatisticDTO.getFoodWaste());
    }
}

