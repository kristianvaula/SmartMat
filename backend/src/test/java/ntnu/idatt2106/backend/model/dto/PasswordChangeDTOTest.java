package ntnu.idatt2106.backend.model.dto;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordChangeDTOTest {
    @Test
    public void testEquals() {
        PasswordChangeDTO cart1 = new PasswordChangeDTO();
        cart1.setNewPassword("1L");

        PasswordChangeDTO cart2 = new PasswordChangeDTO();
        cart2.setNewPassword("1L");

        PasswordChangeDTO cart3 = new PasswordChangeDTO();
        cart3.setNewPassword("2L");

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        PasswordChangeDTO cart1 = new PasswordChangeDTO();
        cart1.setNewPassword("1L");

        PasswordChangeDTO cart2 = new PasswordChangeDTO();
        cart2.setNewPassword("1L");

        PasswordChangeDTO cart3 = new PasswordChangeDTO();
        cart3.setNewPassword("2L");

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String newPassword = "1L";
        String password = "password";
        PasswordChangeDTO user = new PasswordChangeDTO();
        user.setOldPassword(password);
        user.setNewPassword(newPassword);

        // Assert
        assertEquals(user.getNewPassword(), newPassword);
        assertEquals(user.getOldPassword(), password);
    }

    @Test
    public void testToString() {
        // Arrange
        String newPassword = "1L";
        String password = "password";
        PasswordChangeDTO user = new PasswordChangeDTO();
        user.setOldPassword(password);
        user.setNewPassword(newPassword);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("oldPassword=" + password));
        assertTrue(result.contains("newPassword=" + newPassword));
    }
}
