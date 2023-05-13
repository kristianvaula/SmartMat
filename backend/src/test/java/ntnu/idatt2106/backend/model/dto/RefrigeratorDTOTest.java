package ntnu.idatt2106.backend.model.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefrigeratorDTOTest {
    @Test
    public void testEquals() {
        RefrigeratorDTO cart1 = new RefrigeratorDTO();
        cart1.setId(1L);

        RefrigeratorDTO cart2 = new RefrigeratorDTO();
        cart2.setId(1L);

        RefrigeratorDTO cart3 = new RefrigeratorDTO();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        RefrigeratorDTO cart1 = new RefrigeratorDTO();
        cart1.setId(1L);

        RefrigeratorDTO cart2 = new RefrigeratorDTO();
        cart2.setId(1L);

        RefrigeratorDTO cart3 = new RefrigeratorDTO();
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
        List<MemberDTO> memberList = new ArrayList<>();
        RefrigeratorDTO refrigerator = new RefrigeratorDTO();
        refrigerator.setId(id);
        refrigerator.setName(name);
        refrigerator.setAddress(address);
        refrigerator.setMembers(memberList);

        // Assert
        assertEquals(refrigerator.getId(), id);
        assertEquals(refrigerator.getAddress(), address);
        assertEquals(refrigerator.getName(), name);
        assertEquals(refrigerator.getMembers(), memberList);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        String address = "address";
        RefrigeratorDTO refrigerator = new RefrigeratorDTO();
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
