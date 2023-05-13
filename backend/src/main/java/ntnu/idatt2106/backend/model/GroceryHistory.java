package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


/**
 * Model of a historic RefrigeratorGrocery object. This is a RefrigeratorGrocery object that has been consumed and is to be added to stats.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GroceryHistory")
@Schema(description = "An entity represents a previously existing grocery")
@Entity
public class GroceryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the refrigerator where the grocery existed, automatically generated")
    private long id;

    @NotNull
    @Column(name = "dateConsumed")
    @Schema(description = "Date when grocery was consumed")
    private LocalDate dateConsumed;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "refrigeratorId")
    @Schema(description = "The refrigerator the grocery is in")
    private Refrigerator refrigerator;

    @NotNull
    @Column(name = "wasTrashed")
    private boolean wasTrashed;

    @NotNull
    @Column(name = "weightInGrams")
    private Integer weightInGrams;

}
