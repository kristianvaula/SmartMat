package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;

/**
 * DTO shopping list element
 */
public class ShoppingListElementDTO {
    private long id;
    private long groceryId;
    private String description;
    private int quantity;
    private UnitDTO unitDTO;
    private String categoryName;
    private boolean requested;

    /**
     * Constructor to create a ShoppingListElementDTO from a grocery item on the shopping list
     * @param element  grocery item from the shopping list
     */
    public ShoppingListElementDTO(GroceryShoppingList element) {
        this.id = element.getId();
        this.groceryId = element.getGrocery().getId();
        this.description = element.getGrocery().getDescription();
        this.quantity = element.getQuantity();
        this.unitDTO = new UnitDTO(element.getUnit());
        this.categoryName = element.getGrocery().getSubCategory().getCategory().getName();
        this.requested = element.isRequest();
    }

    /**
     * Getter for shopping list element id
     * @return shopping list element id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for grocery id
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
     * Getter for quantity of grocery
     * @return quantity of grocery
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter for unit
     * @return unit
     */
    public UnitDTO getUnitDTO() {
        return unitDTO;
    }

    /**
     * Getter for category name
     * @return category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Getter for if the grocery is added by a SUPERUSER or USER
     * @return true if the grocery is added by a USER and false if the grocery is added by a SUPERUSER
     */
    public boolean isRequested() {
        return requested;
    }
}
