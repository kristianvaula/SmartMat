package ntnu.idatt2106.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingListTest {

    @Test
    public void testEquals() {
        ShoppingList cart1 = new ShoppingList();
        cart1.setId(1L);

        ShoppingList cart2 = new ShoppingList();
        cart2.setId(1L);

        ShoppingList cart3 = new ShoppingList();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        ShoppingList cart1 = new ShoppingList();
        cart1.setId(1L);

        ShoppingList cart2 = new ShoppingList();
        cart2.setId(1L);

        ShoppingList cart3 = new ShoppingList();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        Refrigerator refrigerator = new Refrigerator();
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(id);
        shoppingList.setRefrigerator(refrigerator);

        // Assert
        assertEquals(shoppingList.getId(), id);
        assertEquals(shoppingList.getRefrigerator(), refrigerator);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        Refrigerator refrigerator = new Refrigerator();
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(id);
        shoppingList.setRefrigerator(refrigerator);

        // Act
        String result = shoppingList.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("refrigerator=" + refrigerator.toString()));
    }

    @Test
    public void testShoppingListBuilder() {
        Refrigerator refrigerator = new Refrigerator();
        ShoppingList shoppingList = ShoppingList.builder()
                .refrigerator(refrigerator)
                .build();

        assertNotNull(shoppingList);
        assertEquals(refrigerator, shoppingList.getRefrigerator());
    }
}
