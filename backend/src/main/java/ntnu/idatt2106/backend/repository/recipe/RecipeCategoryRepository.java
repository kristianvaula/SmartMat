package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


/**
 * Repository for RecipeCategory entity.
 */
@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {


    /**
     * Returns a optional RecipeCategory by name.
     * @param name
     * @return
     */
    Optional<RecipeCategory> findByName(String name);

    /**
     * Returns a boolean wether or not category exists by name.
     * @param category
     * @return
     */
    boolean existsByName(String category);
}