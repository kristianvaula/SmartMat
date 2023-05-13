package ntnu.idatt2106.backend.model.recipe;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.persistence.*;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.Grocery;


/**
 * Connection entity model between a Recipe and a Grocery. Holds the required unit/quantity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RecipeGrocery")
@Schema(description = "A relationship between a recipe and its grocery items")
public class RecipeGrocery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the RecipeGrocery entry, automatically generated")
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipeID", nullable = false)
    @Schema(description = "The recipe in the relationship")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "groceryID", nullable = false)
    @Schema(description = "The grocery in the relationship")
    private Grocery grocery;

    @Column(name = "quantity", nullable = false)
    @Schema(description = "The quantity of the grocery item in the recipe")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "unitId")
    @Schema(description = "The unit of a grocery")
    private Unit unit;
}
