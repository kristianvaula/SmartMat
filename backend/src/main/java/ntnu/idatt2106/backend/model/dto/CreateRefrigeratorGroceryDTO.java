package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A DTO for a request to create a Refrigerator Grocery. Holds a RefrigeratorGroceryDTO, the quantity and the unitDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRefrigeratorGroceryDTO {

    private GroceryDTO groceryDTO;
    private UnitDTO unitDTO;
    private Integer quantity;

}
