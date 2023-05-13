package ntnu.idatt2106.backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    @Test
    public void testEquals() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(1L);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.setId(1L);

        ShoppingCart cart3 = new ShoppingCart();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(1L);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.setId(1L);

        ShoppingCart cart3 = new ShoppingCart();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(1L);
        shoppingCart.setId(1L);
        shoppingCart.setShoppingList(shoppingList);

        // Act
        long id = shoppingCart.getId();
        ShoppingList resultShoppingList = shoppingCart.getShoppingList();

        // Assert
        assertEquals(1L, id);
        assertEquals(shoppingList, resultShoppingList);
    }

    @Test
    public void testToString() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(1L);
        shoppingCart.setId(1L);
        shoppingCart.setShoppingList(shoppingList);

        // Act
        String result = shoppingCart.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("shoppingList=" + shoppingList.toString()));
    }

    @Test
    void testShoppingCartBuilder() {
        long id = 1L;
        ShoppingList shoppingList = ShoppingList.builder().id(2L).build();

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .id(id)
                .shoppingList(shoppingList)
                .build();

        assertEquals(id, shoppingCart.getId());
        assertEquals(shoppingList, shoppingCart.getShoppingList());
    }
}
