package ntnu.idatt2106.backend.integration.recipe;

import java.util.Arrays;

import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.*;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecipeGroceryRepository recipeGroceryRepository;

    @Autowired
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Autowired
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    public void setUp() {
        shoppingCartRepository.deleteAll();
        shoppingListRepository.deleteAll();
        refrigeratorUserRepository.deleteAll();
        refrigeratorGroceryRepository.deleteAll();
        refrigeratorRepository.deleteAll();
        recipeGroceryRepository.deleteAll();
        recipeRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
        createRecipeCategories();
        createRecipes();
    }

    private void createRecipeCategories() {
        String[] categories = {"Breakfast", "Lunch", "Dinner", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            recipeCategoryRepository.save(RecipeCategory.builder()
                    .name(category).build());
        });
    }

    private RecipeCategory getRecipeCategoryByName(String name) {
        return recipeCategoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Recipe category not found: " + name));
    }

    private void createRecipes() {
        String[] recipeNames = {"Eggerøre", "Ostesmørbrød", "Pølse i brød"};
        RecipeCategory[] categories = {
                getRecipeCategoryByName("Breakfast"),
                getRecipeCategoryByName("Lunch"),
                getRecipeCategoryByName("Dinner")
        };

        for (int i = 0; i < recipeNames.length; i++) {
            if (!recipeRepository.existsByName(recipeNames[i])) {
                recipeRepository.save(Recipe.builder()
                        .name(recipeNames[i])
                        .category(categories[i])
                        .build());
            }
        }
    }

    @Test
    public void testRecipeDataExists() {
        RecipeCategory breakfastCategory = getRecipeCategoryByName("Breakfast");
        RecipeCategory lunchCategory = getRecipeCategoryByName("Lunch");
        RecipeCategory dinnerCategory = getRecipeCategoryByName("Dinner");

        Recipe recipe1 = recipeRepository.findByName("Eggerøre")
                .orElseThrow(() -> new RuntimeException("Recipe not found: Eggerøre"));
        Recipe recipe2 = recipeRepository.findByName("Ostesmørbrød")
                .orElseThrow(() -> new RuntimeException("Recipe not found: Ostesmørbrød"));
        Recipe recipe3 = recipeRepository.findByName("Pølse i brød")
                .orElseThrow(() -> new RuntimeException("Recipe not found: Pølse i brød"));

        assertAll("Recipe data",
                () -> assertEquals(recipe1.getCategory(), breakfastCategory),
                () -> assertEquals(recipe2.getCategory(), lunchCategory),
                () -> assertEquals(recipe3.getCategory(), dinnerCategory)
        );
    }
}
