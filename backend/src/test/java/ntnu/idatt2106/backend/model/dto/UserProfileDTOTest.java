package ntnu.idatt2106.backend.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserProfileDTOTest {
    @Test
    public void testEquals() {
        UserProfileDTO  cart1 = new UserProfileDTO ();
        cart1.setName("1L");

        UserProfileDTO  cart2 = new UserProfileDTO ();
        cart2.setName("1L");

        UserProfileDTO  cart3 = new UserProfileDTO ();
        cart3.setName("2L");

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        UserProfileDTO  cart1 = new UserProfileDTO ();
        cart1.setName("1L");

        UserProfileDTO  cart2 = new UserProfileDTO ();
        cart2.setName("1L");

        UserProfileDTO  cart3 = new UserProfileDTO ();
        cart3.setName("2L");

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String name = "name";
        String email = "address";
        long refrigeratorId = 1L;
        UserProfileDTO  user = new UserProfileDTO ();
        user.setName(name);
        user.setEmail(email);
        user.setFavoriteRefrigeratorId(refrigeratorId);

        // Assert
        assertEquals(user.getEmail(), email);
        assertEquals(user.getName(), name);
        assertEquals(user.getFavoriteRefrigeratorId(), refrigeratorId);
    }

    @Test
    public void testToString() {
        // Arrange
        String name = "name";
        String email = "address";
        long refrigeratorId = 1L;
        UserProfileDTO  user = new UserProfileDTO ();
        user.setName(name);
        user.setEmail(email);
        user.setFavoriteRefrigeratorId(refrigeratorId);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("email=" + email));
        assertTrue(result.contains("favoriteRefrigeratorId=" + refrigeratorId));
    }
}
