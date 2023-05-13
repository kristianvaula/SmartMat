package ntnu.idatt2106.backend.model.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteRefrigeratorGroceryDTOTest {
    @Test
    public void testEquals() {
        DeleteRefrigeratorGroceryDTO cart1 = new DeleteRefrigeratorGroceryDTO();
        cart1.setQuantity(1);

        DeleteRefrigeratorGroceryDTO cart2 = new DeleteRefrigeratorGroceryDTO();
        cart2.setQuantity(1);

        DeleteRefrigeratorGroceryDTO cart3 = new DeleteRefrigeratorGroceryDTO();
        cart3.setQuantity(2);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        DeleteRefrigeratorGroceryDTO cart1 = new DeleteRefrigeratorGroceryDTO();
        cart1.setQuantity(1);

        DeleteRefrigeratorGroceryDTO cart2 = new DeleteRefrigeratorGroceryDTO();
        cart2.setQuantity(1);

        DeleteRefrigeratorGroceryDTO cart3 = new DeleteRefrigeratorGroceryDTO();
        cart3.setQuantity(2);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        Integer quantity = 2;
        UnitDTO unitDTO = new UnitDTO();
        DeleteRefrigeratorGroceryDTO dRGDTO = new DeleteRefrigeratorGroceryDTO();
        dRGDTO.setRefrigeratorGroceryDTO(refrigeratorGroceryDTO);
        dRGDTO.setQuantity(quantity);
        dRGDTO.setUnitDTO(unitDTO);

        // Assert
        Assertions.assertEquals(dRGDTO.getRefrigeratorGroceryDTO(), refrigeratorGroceryDTO);
        Assertions.assertEquals(dRGDTO.getQuantity(), quantity);
        Assertions.assertEquals(dRGDTO.getUnitDTO(), unitDTO);
    }

    @Test
    public void testToString() {
        // Arrange
        RefrigeratorGroceryDTO refrigeratorGroceryDTO = new RefrigeratorGroceryDTO();
        Integer quantity = 2;
        UnitDTO unitDTO = new UnitDTO();
        DeleteRefrigeratorGroceryDTO dRGDTO = new DeleteRefrigeratorGroceryDTO();
        dRGDTO.setRefrigeratorGroceryDTO(refrigeratorGroceryDTO);
        dRGDTO.setQuantity(quantity);
        dRGDTO.setUnitDTO(unitDTO);

        // Act
        String result = dRGDTO.toString();

        // Assert
        assertTrue(result.contains("refrigeratorGroceryDTO=" + refrigeratorGroceryDTO));
        assertTrue(result.contains("unitDTO=" + unitDTO.toString()));
        assertTrue(result.contains("quantity=" + quantity));
    }
}
