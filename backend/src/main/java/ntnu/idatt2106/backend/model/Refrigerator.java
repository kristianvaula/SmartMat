package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Refrigerator model. Holds an id, name and address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Refrigerator")
@Schema(description = "A registered refrigerator of the application")
@Entity
public class Refrigerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the refrigerator, automatically generated")
    private long id;

    @NotNull
    @Column(name = "name")
    @Schema(description = "The defined name to the refrigerator")
    private String name;

    @Column(name = "address")
    @Schema(description = "The address to the refrigerator")
    private String address;
}
