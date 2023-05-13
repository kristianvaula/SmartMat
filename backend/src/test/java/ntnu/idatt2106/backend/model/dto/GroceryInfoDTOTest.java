package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.category.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroceryInfoDTOTest {
    @Test
    public void testEquals() {
        GroceryInfoDTO cart1 = new GroceryInfoDTO();
        cart1.setId(1L);

        GroceryInfoDTO cart2 = new GroceryInfoDTO();
        cart2.setId(1L);

        GroceryInfoDTO cart3 = new GroceryInfoDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        GroceryInfoDTO cart1 = new GroceryInfoDTO();
        cart1.setId(1L);

        GroceryInfoDTO cart2 = new GroceryInfoDTO();
        cart2.setId(1L);

        GroceryInfoDTO cart3 = new GroceryInfoDTO();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        String name = "name";
        GroceryInfoDTO subCategory = new GroceryInfoDTO();
        subCategory.setId(id);
        subCategory.setName(name);

        // Assert
        assertEquals(subCategory.getId(), id);
        assertEquals(subCategory.getName(), name);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        GroceryInfoDTO subCategory = new GroceryInfoDTO();
        subCategory.setId(id);
        subCategory.setName(name);

        // Act
        String result = subCategory.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name=" + name));
    }
}
