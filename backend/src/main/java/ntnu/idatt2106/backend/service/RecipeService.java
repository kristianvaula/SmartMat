package ntnu.idatt2106.backend.service;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.dto.GroceryInfoDTO;
import ntnu.idatt2106.backend.model.dto.recipe.IngredientDTO;
import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import ntnu.idatt2106.backend.model.dto.recipe.FetchRecipesDTO;
import ntnu.idatt2106.backend.model.dto.recipe.SimpleGrocery;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.springframework.stereotype.Service;
import ntnu.idatt2106.backend.exceptions.NoSuchElementRuntimeException;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    private final RecipeGroceryRepository recipeGroceryRepository;
    private final RecipeRepository recipeRepository;

    private final Logger logger = Logger.getLogger(RecipeService.class.getName());

    private static int lastDuplicateIndex = 0;


    /**
     * Method to fetch recipes based on what the user has stored in their fridge
     * Also finds the recipes that uses groceries that are about to expire
     */

    public List<RecipeDTO> getSortedRecipesByMatchingGroceries(long refrigeratorId, int numOfRecipesToFetch) {
        List<Recipe> recipes = recipeRepository.findAll();
        Map<Recipe, Integer> recipeMatches = new HashMap<>();

        for (Recipe recipe : recipes) {
            int matches = 0;
            List<RefrigeratorGrocery> refrigeratorGroceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigeratorId);
            Set<Grocery> groceriesInRefrigerator = new HashSet<>();

            for (RefrigeratorGrocery refrigeratorGrocery : refrigeratorGroceries) {
                groceriesInRefrigerator.add(refrigeratorGrocery.getGrocery());
            }

            List<RecipeGrocery> recipeGroceries = recipeGroceryRepository.findAllByRecipe(recipe);
            for (RecipeGrocery recipeGrocery : recipeGroceries) {
                if (groceriesInRefrigerator.contains(recipeGrocery.getGrocery())) {
                    matches++;
                }
            }

            recipeMatches.put(recipe, matches);
        }

        // Sort recipes by the number of matching groceries
        List<Recipe> sortedRecipes = new ArrayList<>(recipes);
        sortedRecipes.sort((recipe1, recipe2) -> recipeMatches.get(recipe2).compareTo(recipeMatches.get(recipe1)));

        List<Recipe> sortedRecipeDTOs = new ArrayList<>();
        int recipesAdded = 0;
        while (recipesAdded < numOfRecipesToFetch) {
            for (Recipe recipe : sortedRecipes) {
                sortedRecipeDTOs.add(recipe);
                recipesAdded++;
                if (recipesAdded >= numOfRecipesToFetch) {
                    break;
                }
            }
        }

        return convertToDTOs(sortedRecipeDTOs);
    }

    public List<RecipeDTO> convertToDTOs(List<Recipe> recipes) {
        return recipes.stream().map(recipe -> {
            RecipeDTO recipeDTO = new RecipeDTO(recipe);
            List<GroceryInfoDTO> groceryInfoList = recipeGroceryRepository.findGroceryInfoByRecipe(recipe);

            // Convert the GroceryInfoDTO list to a list of IngredientDTOs
            List<IngredientDTO> ingredients = groceryInfoList.stream()
                    .map(gi -> {
                        SimpleGrocery grocery = new SimpleGrocery(gi.getId(), gi.getName());
                        Grocery groceryForSearch = new Grocery();
                        groceryForSearch.setId(gi.getId());
                        groceryForSearch.setName(gi.getName());

                        RecipeGrocery recipeGrocery = recipeGroceryRepository
                                .findFirstByRecipeAndGrocery(recipe, groceryForSearch)
                                .orElseThrow(() -> new NoSuchElementRuntimeException("No matching RecipeGrocery found."));

                        return new IngredientDTO(grocery, recipeGrocery.getQuantity(), recipeGrocery.getUnit());
                    })
                    .collect(Collectors.toList());

            recipeDTO.setIngredients(ingredients);
            return recipeDTO;
        }).toList();
    }

    /**
     * Find recipe by id
     * @param recipeId recipe id
     * @return the recipe
     * @throws NoSuchElementException if no recipe
     */
    public Recipe getRecipeById(long recipeId) throws NoSuchElementException{
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("Could not find recipe"));
    }

    /**
     * Find all ingredients in a recipe
     * @param recipe the recipe
     * @return ingredients list of RecipeGrocery
     */
    public List<RecipeGrocery> getIngredientsByRecipe(Recipe recipe) {
        return recipeGroceryRepository.findAllByRecipe(recipe);
    }

    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return convertToDTOs(recipes);
    }

    public void setRecipeCategoryRepository(RecipeCategoryRepository recipeCategoryRepository) {
    }
}
