package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.SubCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroceryDTOTest {
    @Test
    public void testEquals() {
        GroceryDTO cart1 = new GroceryDTO();
        cart1.setId(1L);

        GroceryDTO cart2 = new GroceryDTO();
        cart2.setId(1L);

        GroceryDTO cart3 = new GroceryDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        GroceryDTO cart1 = new GroceryDTO();
        cart1.setId(1L);

        GroceryDTO cart2 = new GroceryDTO();
        cart2.setId(1L);

        GroceryDTO cart3 = new GroceryDTO();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        String name = "name";
        int groceryExpiryDays = 2;
        String description = "desc";
        SubCategory subCategory = new SubCategory();
        boolean custom = false;
        GroceryDTO groceryDTO = new GroceryDTO();
        groceryDTO.setId(id);
        groceryDTO.setName(name);
        groceryDTO.setGroceryExpiryDays(groceryExpiryDays);
        groceryDTO.setDescription(description);
        groceryDTO.setCustom(custom);
        groceryDTO.setSubCategory(subCategory);

        // Assert
        assertEquals(groceryDTO.getId(), id);
        assertEquals(groceryDTO.getName(), name);
        assertEquals(groceryDTO.getGroceryExpiryDays(), groceryExpiryDays);
        assertEquals(groceryDTO.getDescription(), description);
        assertEquals(groceryDTO.getSubCategory(), subCategory);
        assertEquals(groceryDTO.isCustom(), custom);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        int groceryExpiryDays = 2;
        String description = "desc";
        SubCategory subCategory = new SubCategory();
        boolean custom = false;
        GroceryDTO groceryDTO = new GroceryDTO();
        groceryDTO.setId(id);
        groceryDTO.setName(name);
        groceryDTO.setGroceryExpiryDays(groceryExpiryDays);
        groceryDTO.setDescription(description);
        groceryDTO.setCustom(custom);
        groceryDTO.setSubCategory(subCategory);

        // Act
        String result = groceryDTO.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("description=" + description));
        assertTrue(result.contains("groceryExpiryDays=" + groceryExpiryDays));
        assertTrue(result.contains("subCategory=" + subCategory));
        assertTrue(result.contains("custom=" + custom));
    }
}
