package ntnu.idatt2106.backend.model.dto.recipe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for recipe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private long id;
    private String name;
    private String url;
    private List<IngredientDTO> ingredients = new ArrayList<>();

    /**
     * Copy constructor for recipe
     * @param recipe recipe to copy
     */
    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.url = recipe.getUrl();
    }
}
