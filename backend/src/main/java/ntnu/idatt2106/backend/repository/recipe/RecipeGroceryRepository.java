package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.dto.GroceryInfoDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for RecipeGrocery entity.
 */
@Repository
public interface RecipeGroceryRepository extends JpaRepository<RecipeGrocery, Long> {

    /**
     * Returns boolean wether or not RecipeGrocery exists based on recipe and grocery connection.
     * @param recipe
     * @param grocery
     * @return
     */
    boolean existsByRecipeAndGrocery(Recipe recipe, Grocery grocery);

    /**
     * Returns all recipies.
     * @param recipe
     * @return
     */
    List<RecipeGrocery> findAllByRecipe(Recipe recipe);

    /**
     * Queries the database and returns a list of dtos for all groceries for a recipe.
     * @param recipe
     * @return
     */
    @Query("SELECT new ntnu.idatt2106.backend.model.dto.GroceryInfoDTO(g.id, g.name) FROM RecipeGrocery rg JOIN rg.grocery g WHERE rg.recipe = :recipe")
    List<GroceryInfoDTO> findGroceryInfoByRecipe(Recipe recipe);

    /**
     * Returns an optional for the first recipe by recipe and grocery.
     * @param recipe
     * @param grocery
     * @return
     */
    Optional<RecipeGrocery> findFirstByRecipeAndGrocery(Recipe recipe, Grocery grocery);

    /**
     * Returns an optional by the recipe id.
     * @param id
     * @return
     */
    Optional<RecipeGrocery> findByRecipeId(long id);

}
