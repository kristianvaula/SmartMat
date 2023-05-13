package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.dto.GroceryNotificationDTO;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroceryNotificationDTOTest {
    @Test
    public void testEquals() {
        GroceryNotificationDTO cart1 = new GroceryNotificationDTO();
        cart1.setId(1L);

        GroceryNotificationDTO cart2 = new GroceryNotificationDTO();
        cart2.setId(1L);

        GroceryNotificationDTO cart3 = new GroceryNotificationDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        GroceryNotificationDTO cart1 = new GroceryNotificationDTO();
        cart1.setId(1L);

        GroceryNotificationDTO cart2 = new GroceryNotificationDTO();
        cart2.setId(1L);

        GroceryNotificationDTO cart3 = new GroceryNotificationDTO();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
        long daysLeft = 2L;
        GroceryNotificationDTO groceryNotificationDTO = new GroceryNotificationDTO();
        groceryNotificationDTO.setId(id);
        groceryNotificationDTO.setRefrigeratorGrocery(refrigeratorGrocery);
        groceryNotificationDTO.setDaysLeft(daysLeft);

        // Assert
        assertEquals(groceryNotificationDTO.getRefrigeratorGrocery(), refrigeratorGrocery);
        assertEquals(groceryNotificationDTO.getId(), id);
        assertEquals(groceryNotificationDTO.getDaysLeft(), daysLeft);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
        long daysLeft = 2L;
        GroceryNotificationDTO groceryNotificationDTO = new GroceryNotificationDTO();
        groceryNotificationDTO.setId(id);
        groceryNotificationDTO.setRefrigeratorGrocery(refrigeratorGrocery);
        groceryNotificationDTO.setDaysLeft(daysLeft);

        // Act
        String result = groceryNotificationDTO.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("refrigeratorGrocery=" + refrigeratorGrocery));
        assertTrue(result.contains("daysLeft=" + daysLeft));
    }
}
