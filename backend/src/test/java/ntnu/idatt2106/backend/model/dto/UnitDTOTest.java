package ntnu.idatt2106.backend.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitDTOTest {
    @Test
    public void testEquals() {
        UnitDTO cart1 = new UnitDTO();
        cart1.setId(1L);

        UnitDTO cart2 = new UnitDTO();
        cart2.setId(1L);

        UnitDTO cart3 = new UnitDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        UnitDTO cart1 = new UnitDTO();
        cart1.setId(1L);

        UnitDTO cart2 = new UnitDTO();
        cart2.setId(1L);

        UnitDTO cart3 = new UnitDTO();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        String name = "name";
        int weight = 2;
        UnitDTO subCategory = new UnitDTO();
        subCategory.setId(id);
        subCategory.setName(name);
        subCategory.setWeight(weight);

        // Assert
        assertEquals(subCategory.getId(), id);
        assertEquals(subCategory.getName(), name);
        assertEquals(subCategory.getWeight(), weight);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        int weight = 2;
        UnitDTO subCategory = new UnitDTO();
        subCategory.setId(id);
        subCategory.setName(name);
        subCategory.setWeight(weight);

        // Act
        String result = subCategory.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("weight=" + weight));
    }

}
