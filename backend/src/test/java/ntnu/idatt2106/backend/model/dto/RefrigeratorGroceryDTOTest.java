package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefrigeratorGroceryDTOTest {
    @Test
    public void testEquals() {
        RefrigeratorGroceryDTO cart1 = new RefrigeratorGroceryDTO();
        cart1.setId(1L);

        RefrigeratorGroceryDTO cart2 = new RefrigeratorGroceryDTO();
        cart2.setId(1L);

        RefrigeratorGroceryDTO cart3 = new RefrigeratorGroceryDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        RefrigeratorGroceryDTO cart1 = new RefrigeratorGroceryDTO();
        cart1.setId(1L);

        RefrigeratorGroceryDTO cart2 = new RefrigeratorGroceryDTO();
        cart2.setId(1L);

        RefrigeratorGroceryDTO cart3 = new RefrigeratorGroceryDTO();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        GroceryDTO groceryDTO = new GroceryDTO();
        LocalDate physicalExpireDate = LocalDate.now();
        int quantity = 2;
        UnitDTO unitDTO = new UnitDTO();
        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setId(id);
        refrigeratorGroceryDTO.setGrocery(groceryDTO);
        refrigeratorGroceryDTO.setQuantity(quantity);
        refrigeratorGroceryDTO.setUnit(unitDTO);
        refrigeratorGroceryDTO.setPhysicalExpireDate(physicalExpireDate);

        // Assert
        assertEquals(refrigeratorGroceryDTO.getId(), id);
        assertEquals(refrigeratorGroceryDTO.getGrocery(), groceryDTO);
        assertEquals(refrigeratorGroceryDTO.getQuantity(), quantity);
        assertEquals(refrigeratorGroceryDTO.getUnit(), unitDTO);
        assertEquals(refrigeratorGroceryDTO.getPhysicalExpireDate(), physicalExpireDate);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        GroceryDTO groceryDTO = new GroceryDTO();
        LocalDate physicalExpireDate = LocalDate.now();
        int quantity = 2;
        UnitDTO unitDTO = new UnitDTO();
        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        refrigeratorGroceryDTO.setId(id);
        refrigeratorGroceryDTO.setGrocery(groceryDTO);
        refrigeratorGroceryDTO.setQuantity(quantity);
        refrigeratorGroceryDTO.setUnit(unitDTO);
        refrigeratorGroceryDTO.setPhysicalExpireDate(physicalExpireDate);

        // Act
        String result = refrigeratorGroceryDTO.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("grocery=" + groceryDTO.toString()));
        assertTrue(result.contains("quantity=" + quantity));
    }
}
