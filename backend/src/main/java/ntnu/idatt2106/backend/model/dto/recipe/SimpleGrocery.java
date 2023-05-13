package ntnu.idatt2106.backend.model.dto.recipe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for grocery.
 * The DTO contains only the ID and name for the grocery
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGrocery {
    private long id;
    private String name;
}
