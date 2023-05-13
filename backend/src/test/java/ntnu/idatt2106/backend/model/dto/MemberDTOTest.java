package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberDTOTest {
    @Test
    public void testEquals() {
        MemberDTO cart1 = new MemberDTO();
        cart1.setRefrigeratorId(1L);

        MemberDTO cart2 = new MemberDTO();
        cart2.setRefrigeratorId(1L);

        MemberDTO cart3 = new MemberDTO();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        MemberDTO cart1 = new MemberDTO();
        cart1.setRefrigeratorId(1L);

        MemberDTO cart2 = new MemberDTO();
        cart2.setRefrigeratorId(1L);

        MemberDTO cart3 = new MemberDTO();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String name = "name";
        String email = "address";
        String password = "password";
        FridgeRole userRole = FridgeRole.USER;
        long refrigeratorId = 1L;
        MemberDTO user = new MemberDTO();
        user.setRefrigeratorId(refrigeratorId);
        user.setName(name);
        user.setUsername(email);
        user.setFridgeRole(userRole);

        // Assert
        assertEquals(user.getRefrigeratorId(), refrigeratorId);
        assertEquals(user.getUsername(), email);
        assertEquals(user.getName(), name);
        assertEquals(user.getFridgeRole(), userRole);
    }

    @Test
    public void testToString() {
        // Arrange
        String name = "name";
        String email = "address";
        String password = "password";
        FridgeRole userRole = FridgeRole.USER;
        long refrigeratorId = 1L;
        MemberDTO user = new MemberDTO();
        user.setRefrigeratorId(refrigeratorId);
        user.setName(name);
        user.setUsername(email);
        user.setFridgeRole(userRole);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("refrigeratorId=" + refrigeratorId));
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("username=" + email));
        assertTrue(result.contains("fridgeRole=" + userRole));
    }
}
