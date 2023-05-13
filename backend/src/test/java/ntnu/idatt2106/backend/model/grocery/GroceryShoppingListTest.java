package ntnu.idatt2106.backend.model.grocery;


import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroceryShoppingListTest {

    @Test
    void testNoArgsConstructor() {
        GroceryShoppingList groceryShoppingList = new GroceryShoppingList();
        assertEquals(0, groceryShoppingList.getId());
        assertFalse(groceryShoppingList.isRequest());
        assertEquals(0, groceryShoppingList.getQuantity());
        assertNull(groceryShoppingList.getUnit());
        assertNull(groceryShoppingList.getGrocery());
        assertNull(groceryShoppingList.getShoppingList());
    }

    @Test
    void testAllArgsConstructor() {
        long id = 1L;
        boolean isRequest = true;
        int quantity = 2;
        Unit unit = Unit.builder().name("1").id(1L).weight(100).build();
        Grocery grocery = new Grocery();
        ShoppingList shoppingList = new ShoppingList();

        GroceryShoppingList groceryShoppingList = new GroceryShoppingList(id, isRequest, quantity, unit, grocery, shoppingList);
        assertEquals(id, groceryShoppingList.getId());
        assertTrue(groceryShoppingList.isRequest());
        assertEquals(quantity, groceryShoppingList.getQuantity());
        assertEquals(unit, groceryShoppingList.getUnit());
        assertEquals(grocery, groceryShoppingList.getGrocery());
        assertEquals(shoppingList, groceryShoppingList.getShoppingList());
    }

    @Test
    void testEditQuantity() {
        GroceryShoppingList groceryShoppingList = new GroceryShoppingList();
        groceryShoppingList.setQuantity(2);
        groceryShoppingList.setUnit(Unit.builder().name("1").id(1L).weight(100).build());

        // Adding 1 kg with same unit
        assertEquals(22, groceryShoppingList.editQuantity(1, Unit.builder().name("2").id(2L).weight(1000).build()));

        // Adding 1 liter with different unit, should convert to kg
        assertEquals(32, groceryShoppingList.editQuantity(1,Unit.builder().name("3").id(3L).weight(1000).build()));

        // Adding 1 gram with different unit, should convert to kg
        assertEquals(2201, groceryShoppingList.editQuantity(1,Unit.builder().name("4").id(4L).weight(1).build()));

    }
}
