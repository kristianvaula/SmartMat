package ntnu.idatt2106.backend.model.grocery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2106.backend.model.ShoppingCart;
import ntnu.idatt2106.backend.model.Unit;

public class GroceryShoppingCartTest {

    private GroceryShoppingCart groceryShoppingCart;
    private Unit unit;
    private Grocery grocery;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        unit = new Unit();
        grocery = new Grocery();
        shoppingCart = new ShoppingCart();
        groceryShoppingCart = GroceryShoppingCart.builder()
                .quantity(3)
                .unit(unit)
                .grocery(grocery)
                .shoppingCart(shoppingCart)
                .build();
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(3, groceryShoppingCart.getQuantity());
        assertEquals(unit, groceryShoppingCart.getUnit());
        assertEquals(grocery, groceryShoppingCart.getGrocery());
        assertEquals(shoppingCart, groceryShoppingCart.getShoppingCart());

        int newQuantity = 5;
        groceryShoppingCart.setQuantity(newQuantity);
        assertEquals(newQuantity, groceryShoppingCart.getQuantity());

        Unit newUnit = new Unit();
        groceryShoppingCart.setUnit(newUnit);
        assertEquals(newUnit, groceryShoppingCart.getUnit());

        Grocery newGrocery = new Grocery();
        groceryShoppingCart.setGrocery(newGrocery);
        assertEquals(newGrocery, groceryShoppingCart.getGrocery());

        ShoppingCart newShoppingCart = new ShoppingCart();
        groceryShoppingCart.setShoppingCart(newShoppingCart);
        assertEquals(newShoppingCart, groceryShoppingCart.getShoppingCart());
    }

    @Test
    void testEditQuantity() {
        int newQuantity = 5;
        assertEquals(8, groceryShoppingCart.editQuantity(newQuantity));

        assertEquals(8, groceryShoppingCart.getQuantity());

        assertEquals(8, groceryShoppingCart.editQuantity(-3));

        assertEquals(8, groceryShoppingCart.getQuantity());
    }

    @Test
    void testIdGeneration() {
        assertEquals(0L, groceryShoppingCart.getId());

        GroceryShoppingCart cartWithId = GroceryShoppingCart.builder()
                .id(123L)
                .quantity(3)
                .unit(unit)
                .grocery(grocery)
                .shoppingCart(shoppingCart)
                .build();

        assertEquals(123L, cartWithId.getId());
    }
}
