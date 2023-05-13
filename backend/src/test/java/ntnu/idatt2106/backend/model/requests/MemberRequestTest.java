package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberRequestTest {
    @Test
    public void testEquals() {
        MemberRequest cart1 = new MemberRequest();
        cart1.setRefrigeratorId(1L);

        MemberRequest cart2 = new MemberRequest();
        cart2.setRefrigeratorId(1L);

        MemberRequest cart3 = new MemberRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        MemberRequest cart1 = new MemberRequest();
        cart1.setRefrigeratorId(1L);

        MemberRequest cart2 = new MemberRequest();
        cart2.setRefrigeratorId(1L);

        MemberRequest cart3 = new MemberRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String userName = "userName";
        FridgeRole fridgeRole = FridgeRole.USER.USER;
        long refrigeratorId = 1L;
        MemberRequest user = new MemberRequest();
        user.setRefrigeratorId(refrigeratorId);
        user.setUserName(userName);
        user.setFridgeRole(fridgeRole);

        // Assert
        assertEquals(user.getRefrigeratorId(), refrigeratorId);
        assertEquals(user.getUserName(), userName);
        assertEquals(user.getFridgeRole(), fridgeRole);
    }

    @Test
    public void testToString() {
        // Arrange
        String userName = "userName";
        FridgeRole fridgeRole = FridgeRole.USER.USER;
        long refrigeratorId = 1L;
        MemberRequest user = new MemberRequest();
        user.setRefrigeratorId(refrigeratorId);
        user.setUserName(userName);
        user.setFridgeRole(fridgeRole);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("refrigeratorId=" + refrigeratorId));
        assertTrue(result.contains("userName=" + userName));
        assertTrue(result.contains("fridgeRole=" + fridgeRole.toString()));
    }

    @Test
    void testBuilder() {
        // Create a MemberRequest using the builder pattern
        MemberRequest memberRequest = MemberRequest.builder()
                .refrigeratorId(123L)
                .userName("johndoe")
                .fridgeRole(FridgeRole.USER)
                .build();

        // Verify that the fields were set correctly
        assertEquals(123L, memberRequest.getRefrigeratorId());
        assertEquals("johndoe", memberRequest.getUserName());
        assertEquals(FridgeRole.USER, memberRequest.getFridgeRole());
    }
}
