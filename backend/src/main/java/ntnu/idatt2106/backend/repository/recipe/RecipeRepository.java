package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for Recipe entity.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    /**
     * Returns an optional Recipe based on a recipe name.
     * @param name
     * @return
     */
    Optional<Recipe> findByName(String name);

    /**
     * Returns a boolean if a recipe exists based on name.
     * @param recipeName
     * @return
     */
    boolean existsByName(String recipeName);
}