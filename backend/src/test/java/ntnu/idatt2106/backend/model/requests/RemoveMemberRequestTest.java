package ntnu.idatt2106.backend.model.requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoveMemberRequestTest {
    @Test
    public void testEquals() {
        RemoveMemberRequest cart1 = new RemoveMemberRequest();
        cart1.setRefrigeratorId(1L);

        RemoveMemberRequest cart2 = new RemoveMemberRequest();
        cart2.setRefrigeratorId(1L);

        RemoveMemberRequest cart3 = new RemoveMemberRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        RemoveMemberRequest cart1 = new RemoveMemberRequest();
        cart1.setRefrigeratorId(1L);

        RemoveMemberRequest cart2 = new RemoveMemberRequest();
        cart2.setRefrigeratorId(1L);

        RemoveMemberRequest cart3 = new RemoveMemberRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String removeMemberRequestName = "removeMemberRequestName";
        boolean forceDelete = false;
        long refrigeratorId = 1L;
        RemoveMemberRequest removeMemberRequest = new RemoveMemberRequest();
        removeMemberRequest.setRefrigeratorId(refrigeratorId);
        removeMemberRequest.setUserName(removeMemberRequestName);
        removeMemberRequest.setForceDelete(forceDelete);

        // Assert
        assertEquals(removeMemberRequest.getRefrigeratorId(), refrigeratorId);
        assertEquals(removeMemberRequest.getUserName(), removeMemberRequestName);
        assertEquals(removeMemberRequest.isForceDelete(), forceDelete);
    }

    @Test
    public void testToString() {
        // Arrange
        String removeMemberRequestName = "removeMemberRequestName";
        boolean forceDelete = false;
        long refrigeratorId = 1L;
        RemoveMemberRequest removeMemberRequest = new RemoveMemberRequest();
        removeMemberRequest.setRefrigeratorId(refrigeratorId);
        removeMemberRequest.setUserName(removeMemberRequestName);
        removeMemberRequest.setForceDelete(forceDelete);

        // Act
        String result = removeMemberRequest.toString();

        // Assert
        assertTrue(result.contains("refrigeratorId=" + refrigeratorId));
        assertTrue(result.contains("userName=" + removeMemberRequestName));
        assertTrue(result.contains("forceDelete=" + forceDelete));
    }

    @Test
    public void testBuilder() {
        // Given
        long refrigeratorId = 1L;
        String userName = "johndoe";
        boolean forceDelete = true;

        // When
        RemoveMemberRequest removeMemberRequest = RemoveMemberRequest.builder()
                .refrigeratorId(refrigeratorId)
                .userName(userName)
                .forceDelete(forceDelete)
                .build();

        // Then
        assertNotNull(removeMemberRequest);
        assertEquals(refrigeratorId, removeMemberRequest.getRefrigeratorId());
        assertEquals(userName, removeMemberRequest.getUserName());
        assertEquals(forceDelete, removeMemberRequest.isForceDelete());
    }
}
