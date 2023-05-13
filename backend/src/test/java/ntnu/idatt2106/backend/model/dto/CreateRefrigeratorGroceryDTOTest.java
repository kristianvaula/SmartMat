package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.dto.CreateRefrigeratorGroceryDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateRefrigeratorGroceryDTOTest {
    @Test
    public void testEquals() {
        CreateRefrigeratorGroceryDTO cart1 = new CreateRefrigeratorGroceryDTO();
        cart1.setQuantity(1);

        CreateRefrigeratorGroceryDTO cart2 = new CreateRefrigeratorGroceryDTO();
        cart2.setQuantity(1);

        CreateRefrigeratorGroceryDTO cart3 = new CreateRefrigeratorGroceryDTO();
        cart3.setQuantity(2);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        CreateRefrigeratorGroceryDTO cart1 = new CreateRefrigeratorGroceryDTO();
        cart1.setQuantity(1);

        CreateRefrigeratorGroceryDTO cart2 = new CreateRefrigeratorGroceryDTO();
        cart2.setQuantity(1);

        CreateRefrigeratorGroceryDTO cart3 = new CreateRefrigeratorGroceryDTO();
        cart3.setQuantity(2);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        GroceryDTO groceryDTO = new GroceryDTO();
        UnitDTO unitDTO = new UnitDTO();
        int quantity = 1;
        CreateRefrigeratorGroceryDTO createRefrigeratorGroceryDTO = new CreateRefrigeratorGroceryDTO();
        createRefrigeratorGroceryDTO.setQuantity(quantity);
        createRefrigeratorGroceryDTO.setGroceryDTO(groceryDTO);
        createRefrigeratorGroceryDTO.setUnitDTO(unitDTO);

        // Assert
        assertEquals(createRefrigeratorGroceryDTO.getGroceryDTO(), groceryDTO);
        assertEquals(createRefrigeratorGroceryDTO.getUnitDTO(), unitDTO);
        assertEquals(createRefrigeratorGroceryDTO.getQuantity(), quantity);
    }

    @Test
    public void testToString() {
        // Arrange
        GroceryDTO groceryDTO = new GroceryDTO();
        UnitDTO unitDTO = new UnitDTO();
        int quantity = 1;
        CreateRefrigeratorGroceryDTO createRefrigeratorGroceryDTO = new CreateRefrigeratorGroceryDTO();
        createRefrigeratorGroceryDTO.setQuantity(quantity);
        createRefrigeratorGroceryDTO.setGroceryDTO(groceryDTO);
        createRefrigeratorGroceryDTO.setUnitDTO(unitDTO);

        // Act
        String result = createRefrigeratorGroceryDTO.toString();

        // Assert
        assertTrue(result.contains("groceryDTO=" + groceryDTO));
        assertTrue(result.contains("unitDTO=" + unitDTO));
        assertTrue(result.contains("quantity=" + quantity));
    }

}
