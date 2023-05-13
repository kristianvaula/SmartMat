package ntnu.idatt2106.backend.model.dto.shoppingCartElement;

import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;

/**
 * DTO for an item in the shopping cart
 */
public class ShoppingCartElementDTO {
    private long id;
    private long groceryId;
    private String description;
    private int quantity;
    private UnitDTO unitDTO;
    private String categoryName;

    /**
     * Constructor to create a shopping cart from a grocery item in the shopping cart
     * @param element grocery item from shopping cart
     */
    public ShoppingCartElementDTO(GroceryShoppingCart element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.description = element.getGrocery().getDescription();
        this.quantity = element.getQuantity();
        this.unitDTO = new UnitDTO(element.getUnit());
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
    }

    /**
     * Constructor to create a shopping cart from a grocery item in the refrigerator shopping list
     * @param element grocery item from refrigerator shopping list
     */
    public ShoppingCartElementDTO(RefrigeratorShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.description = element.getGrocery().getDescription();
        this.quantity = element.getQuantity();
        this.unitDTO = new UnitDTO(element.getUnit());
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
    }

    /**
     * Getter for id
     * @return id to the shopping cart element
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the grocery id
     * @return grocery id
     */
    public long getGroceryId() {
        return groceryId;
    }

    /**
     * Getter for description
     * @return description of grocery
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for quantity of shopping cart element
     * @return quantity for shopping cart element
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter for unit DTO
     * @return unit DTO
     */
    public UnitDTO getUnitDTO() {
        return unitDTO;
    }

    /**
     * Getter for the category name
     * @return category name
     */
    public String getCategoryName() {
        return categoryName;
    }
}
