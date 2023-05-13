package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.SubCategory;

/**
 * Grocery DTO
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroceryDTO {
    private long id;
    private String name;
    private String description;
    private int groceryExpiryDays;
    private SubCategory subCategory;
    private boolean custom;

    public GroceryDTO(Grocery grocery) {
        this.id = grocery.getId();
        this.name = grocery.getName();
        this.description = grocery.getDescription();
        this.groceryExpiryDays = grocery.getGroceryExpiryDays();
        this.subCategory = grocery.getSubCategory();
    }
}

