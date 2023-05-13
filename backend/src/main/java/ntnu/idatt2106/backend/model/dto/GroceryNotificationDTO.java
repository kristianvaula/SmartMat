package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.grocery.GroceryNotification;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;


/**
 * Grocery notification data transfer object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryNotificationDTO {
    private Long id;
    private RefrigeratorGrocery refrigeratorGrocery;
    private Long daysLeft;

    public GroceryNotificationDTO(GroceryNotification groceryNotification){
        this.id = groceryNotification.getId();
        this.refrigeratorGrocery = groceryNotification.getGroceryEntity();
        this.daysLeft = groceryNotification.getDaysLeft();
    }
}
