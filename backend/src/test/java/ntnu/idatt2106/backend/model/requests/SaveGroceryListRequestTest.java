package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveGroceryListRequestTest {
    @Test
    public void testEquals() {
        SaveGroceryListRequest cart1 = new SaveGroceryListRequest();
        cart1.setRefrigeratorId(1L);

        SaveGroceryListRequest cart2 = new SaveGroceryListRequest();
        cart2.setRefrigeratorId(1L);

        SaveGroceryListRequest cart3 = new SaveGroceryListRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        SaveGroceryListRequest cart1 = new SaveGroceryListRequest();
        cart1.setRefrigeratorId(1L);

        SaveGroceryListRequest cart2 = new SaveGroceryListRequest();
        cart2.setRefrigeratorId(1L);

        SaveGroceryListRequest cart3 = new SaveGroceryListRequest();
        cart3.setRefrigeratorId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        List<GroceryDTO> groceryList = new ArrayList<>();
        long refrigeratorId = 1L;
        SaveGroceryListRequest saveGroceryListRequest = new SaveGroceryListRequest();
        saveGroceryListRequest.setRefrigeratorId(refrigeratorId);
        saveGroceryListRequest.setGroceryList(groceryList);

        // Assert
        assertEquals(saveGroceryListRequest.getRefrigeratorId(), refrigeratorId);
        assertEquals(saveGroceryListRequest.getGroceryList(), groceryList);
    }

    @Test
    public void testToString() {
        // Arrange
        List<GroceryDTO> groceryList = new ArrayList<>();
        long refrigeratorId = 1L;
        SaveGroceryListRequest saveGroceryListRequest = new SaveGroceryListRequest();
        saveGroceryListRequest.setRefrigeratorId(refrigeratorId);
        saveGroceryListRequest.setGroceryList(groceryList);

        // Act
        String result = saveGroceryListRequest.toString();

        // Assert
        assertTrue(result.contains("refrigeratorId=" + refrigeratorId));
        assertTrue(result.contains("groceryList=" + groceryList.toString()));
    }
}
