package ntnu.idatt2106.backend.model.recipe;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


/**
 * Model of a cooking recipe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipe")
@Schema(description = "A recipe in the application")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the recipe, automatically generated")
    private long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "The name of the recipe")
    private String name;

    @Column(name = "url")
    @Schema(description = "The URL of the recipe")
    private String url;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    @Schema(description = "The category of the recipe")
    private RecipeCategory category;
}
