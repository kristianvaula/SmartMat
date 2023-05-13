package ntnu.idatt2106.backend.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;


/**
 * A Request DTO which holds a grocery object that needs to be saved. Is used in transitions between Shopping List, Shopping Cart and Refrigerator.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveGroceryRequest {
    private long groceryId;
    private int quantity;
    private UnitDTO unitDTO;
    private long foreignKey; //can be both shopping list id and shopping cart id

    public SaveGroceryRequest(GroceryShoppingList listItem) {
        this.groceryId = listItem.getGrocery().getId();
        this.quantity = listItem.getQuantity();
        this.unitDTO = new UnitDTO(listItem.getUnit());
        this.foreignKey = listItem.getShoppingList().getId();
    }

    public SaveGroceryRequest(GroceryShoppingCart listItem) {
        this.groceryId = listItem.getGrocery().getId();
        this.quantity = listItem.getQuantity();
        this.unitDTO = new UnitDTO(listItem.getUnit());
        this.foreignKey = listItem.getShoppingCart().getId();
    }

    public SaveGroceryRequest(RefrigeratorShoppingList listItem) {
        this.groceryId = listItem.getGrocery().getId();
        this.quantity = listItem.getQuantity();
        this.unitDTO = new UnitDTO(listItem.getUnit());
        this.foreignKey = listItem.getShoppingList().getId();
    }
}
