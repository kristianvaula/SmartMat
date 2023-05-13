package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A class representing a unit of measurement. Can for example be kilograms.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Unit")
@Schema(description = "Unit of measurement")
@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for a unit")
    private long id;

    @NotNull
    @Column(name = "name")
    @Schema(description = "Name of a unit")
    private String name;

    @NotNull
    @Column(name = "weight")
    @Schema(description = "The weight of one unit in grams")
    private int weight;
}
