package ntnu.idatt2106.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefrigeratorTest {

    @Test
    public void testEquals() {
        Refrigerator cart1 = new Refrigerator();
        cart1.setId(1L);

        Refrigerator cart2 = new Refrigerator();
        cart2.setId(1L);

        Refrigerator cart3 = new Refrigerator();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        Refrigerator cart1 = new Refrigerator();
        cart1.setId(1L);

        Refrigerator cart2 = new Refrigerator();
        cart2.setId(1L);

        Refrigerator cart3 = new Refrigerator();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        String name = "name";
        String address = "address";
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(id);
        refrigerator.setName(name);
        refrigerator.setAddress(address);

        // Assert
        assertEquals(refrigerator.getId(), id);
        assertEquals(refrigerator.getAddress(), address);
        assertEquals(refrigerator.getName(), name);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        String address = "address";
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(id);
        refrigerator.setName(name);
        refrigerator.setAddress(address);

        // Act
        String result = refrigerator.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("address=" + address));
        assertTrue(result.contains("name=" + name));
    }
}
