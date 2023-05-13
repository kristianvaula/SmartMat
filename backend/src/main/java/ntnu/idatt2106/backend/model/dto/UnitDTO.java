package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Unit;


/**
 * DTO for a Unit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitDTO {
    private Long id;
    private String name;
    private Integer weight;

    public UnitDTO(Unit unit){
        this.id = unit.getId();
        this.name = unit.getName();
        this.weight = unit.getWeight();
    }
}
