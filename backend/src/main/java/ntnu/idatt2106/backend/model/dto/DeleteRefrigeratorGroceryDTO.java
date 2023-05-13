package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A DTO for a request to delete a Refrigerator Grocery. Holds a RefrigeratorGroceryDTO, the quantity and the unitDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRefrigeratorGroceryDTO {
    private RefrigeratorGroceryDTO refrigeratorGroceryDTO;
    private Integer quantity;
    private UnitDTO unitDTO;
}
