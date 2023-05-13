package ntnu.idatt2106.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning the statistics for a refrigerator. Represents one month of food eaten and thrown away. Holds a
 * month and year in monthName in following syntax "2023, January".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryStatisticDTO {
    private String monthName;
    private Integer foodEaten;
    private Integer foodWaste;
}
