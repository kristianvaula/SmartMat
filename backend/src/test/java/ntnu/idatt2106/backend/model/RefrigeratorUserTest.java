package ntnu.idatt2106.backend.model;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefrigeratorUserTest {

    @Test
    public void testEquals() {
        RefrigeratorUser cart1 = new RefrigeratorUser();
        cart1.setId(1L);

        RefrigeratorUser cart2 = new RefrigeratorUser();
        cart2.setId(1L);

        RefrigeratorUser cart3 = new RefrigeratorUser();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        RefrigeratorUser cart1 = new RefrigeratorUser();
        cart1.setId(1L);

        RefrigeratorUser cart2 = new RefrigeratorUser();
        cart2.setId(1L);

        RefrigeratorUser cart3 = new RefrigeratorUser();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        FridgeRole role = FridgeRole.USER;
        Refrigerator refrigerator = new Refrigerator();
        User user = new User();
        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(id);
        refrigeratorUser.setFridgeRole(role);
        refrigeratorUser.setRefrigerator(refrigerator);
        refrigeratorUser.setUser(user);

        // Assert
        assertEquals(refrigeratorUser.getId(), id);
        assertEquals(refrigeratorUser.getRefrigerator(), refrigerator);
        assertEquals(refrigeratorUser.getUser(), user);
        assertEquals(refrigeratorUser.getFridgeRole(), role);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        FridgeRole role = FridgeRole.USER;
        Refrigerator refrigerator = new Refrigerator();
        User user = new User();
        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(id);
        refrigeratorUser.setFridgeRole(role);
        refrigeratorUser.setRefrigerator(refrigerator);
        refrigeratorUser.setUser(user);

        // Act
        String result = refrigeratorUser.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("refrigerator=" + refrigerator.toString()));
        assertTrue(result.contains("fridgeRole=" + role.toString()));
        assertTrue(result.contains("user=" + user.toString()));
    }

    @Test
    public void testBuilder() {
        RefrigeratorUser user = RefrigeratorUser.builder()
                .id(1)
                .fridgeRole(FridgeRole.USER)
                .refrigerator(new Refrigerator())
                .user(new User())
                .build();

        assertEquals(1, user.getId());
        assertEquals(FridgeRole.USER, user.getFridgeRole());
        assertEquals(new Refrigerator(), user.getRefrigerator());
        assertEquals(new User(), user.getUser());
    }
}
