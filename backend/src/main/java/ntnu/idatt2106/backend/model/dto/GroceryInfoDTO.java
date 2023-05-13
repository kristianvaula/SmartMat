package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Grocery info DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryInfoDTO {

    private long id;
    private String name;
}
