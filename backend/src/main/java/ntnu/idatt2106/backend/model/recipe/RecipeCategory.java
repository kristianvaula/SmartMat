package ntnu.idatt2106.backend.model.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


/**
 * Recipe Category. Used to categorize recipes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RecipeCategory")
@Schema(description = "A recipe category in the application")
public class RecipeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the recipe category, automatically generated")
    private long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "The name of the recipe category")
    private String name;

    @Column(name = "iconId") // TODO: 2021-10-11 Add default value
    @Schema(description = "The icon ID of the recipe category")
    private String iconId;
}
