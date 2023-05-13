package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.ShoppingCart;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class SaveGroceryRequestTest {

    private final long GROCERY_ID = 1L;
    private final String NAME = "Test Grocery";
    private final int GROCERY_EXPIRY_DAYS = 7;
    private final String DESCRIPTION = "This is a test grocery";
    private final long SUB_CATEGORY_ID = 1L;
    private SubCategory SUB_CATEGORY = new SubCategory();
    private final long FOREIGN_KEY = 1L;
    private final int QUANTITY = 10;
    private final Unit UNIT = Unit.builder()
            .name("dl")
            .id(1)
            .weight(100)
            .build();

    @BeforeEach
    public void setup(){
        SUB_CATEGORY.setId(SUB_CATEGORY_ID);
    }

    @Test
    public void testBuilder() {
        SaveGroceryRequest request = SaveGroceryRequest.builder()
                .groceryId(GROCERY_ID)
                .foreignKey(FOREIGN_KEY)
                .quantity(QUANTITY)
                .build();

        assertNotNull(request);
        assertEquals(GROCERY_ID, request.getGroceryId());
        assertEquals(FOREIGN_KEY, request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testConstructorWithGroceryShoppingList() {
        Grocery grocery = Grocery.builder()
                .id(GROCERY_ID)
                .name(NAME)
                .groceryExpiryDays(GROCERY_EXPIRY_DAYS)
                .description(DESCRIPTION)
                .subCategory(SUB_CATEGORY)
                .build();
        GroceryShoppingList listItem = GroceryShoppingList.builder()
                .grocery(grocery)
                .quantity(QUANTITY)
                .shoppingList(new ShoppingList())
                .unit(UNIT)
                .build();

        SaveGroceryRequest request = new SaveGroceryRequest(listItem);

        assertNotNull(request);
        assertEquals(GROCERY_ID, request.getGroceryId());
        assertEquals(listItem.getShoppingList().getId(), request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testConstructorWithGroceryShoppingCart() {
        Grocery grocery = Grocery.builder()
                .id(GROCERY_ID)
                .name(NAME)
                .groceryExpiryDays(GROCERY_EXPIRY_DAYS)
                .description(DESCRIPTION)
                .subCategory(SUB_CATEGORY)
                .build();
        GroceryShoppingCart listItem = GroceryShoppingCart.builder()
                .grocery(grocery)
                .shoppingCart(new ShoppingCart())
                .quantity(QUANTITY)
                .unit(UNIT)
                .build();

        SaveGroceryRequest request = new SaveGroceryRequest(listItem);

        assertNotNull(request);
        assertEquals(GROCERY_ID, request.getGroceryId());
        assertEquals(listItem.getShoppingCart().getId(), request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testEquals() {
        SaveGroceryRequest cart1 = new SaveGroceryRequest(1L, 1, UnitDTO.builder().build(),1L);
        SaveGroceryRequest cart2 = new SaveGroceryRequest(1L, 1, UnitDTO.builder().build(), 1L);
        SaveGroceryRequest cart3 = new SaveGroceryRequest(2L, 2, UnitDTO.builder().build(), 2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        SaveGroceryRequest cart1 = new SaveGroceryRequest(1L, 1, UnitDTO.builder().build(), 1L);
        SaveGroceryRequest cart2 = new SaveGroceryRequest(1L, 1, UnitDTO.builder().build(), 1L);
        SaveGroceryRequest cart3 = new SaveGroceryRequest(2L, 2, UnitDTO.builder().build(), 2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        SaveGroceryRequest request = new SaveGroceryRequest();
        request.setGroceryId(GROCERY_ID);
        request.setForeignKey(FOREIGN_KEY);
        request.setQuantity(QUANTITY);


        assertEquals(GROCERY_ID, request.getGroceryId());
        assertEquals(FOREIGN_KEY, request.getForeignKey());
        assertEquals(QUANTITY, request.getQuantity());
    }

    @Test
    public void testToString() {
        SaveGroceryRequest request = SaveGroceryRequest.builder()
                .groceryId(1)
                .foreignKey(3)
                .quantity(1)
                .unitDTO(UnitDTO.builder().build())
                .build();

        String expected = "SaveGroceryRequest(groceryId=1, quantity=1, unitDTO=UnitDTO(id=null, name=null, weight=null), foreignKey=3)";
        assertEquals(expected, request.toString());
    }
}
