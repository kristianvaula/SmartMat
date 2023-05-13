package ntnu.idatt2106.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.NoSuchElementException;
import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import ntnu.idatt2106.backend.model.dto.recipe.FetchRecipesDTO;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for recipes
 */
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
@Tag(name = "Recipe controller", description = "Controller to handle the recipes")
public class RecipeController {

    private final Logger logger = Logger.getLogger(RecipeController.class.getName());
    private final RecipeService recipeService;


    /**
     * Fetch recipes based on available groceries and their expiration dates
     * @param refrigeratorId ID to the refrigerator to evaluate groceries from
     * @param numRecipes number of recipes
     * @param fetchedRecipeIds ID to the recipe to fetch from
     * @return list with recipes
     */
    @Operation(summary = "Fetch recipes based on available groceries and their expiration dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/fetch")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchRecipes(
            @Valid @RequestParam("refrigeratorId") long refrigeratorId,
            @Valid @RequestParam("numRecipes") int numRecipes,
            @Valid @RequestParam(value = "recipesFetched", required = false) List<Long> fetchedRecipeIds) {
        if (fetchedRecipeIds == null) {
            fetchedRecipeIds = new ArrayList<>();
        }

        FetchRecipesDTO fetchRecipesDTO = new FetchRecipesDTO();
        fetchRecipesDTO.setRefrigeratorId(refrigeratorId);
        fetchRecipesDTO.setNumRecipes(numRecipes);
        fetchRecipesDTO.setFetchedRecipeIds(fetchedRecipeIds);

        logger.info("Received request to fetch recipes for user");
        logger.info(fetchRecipesDTO.toString());
        List<RecipeDTO> recipes;
        /*if (fetchRecipesDTO.getNumRecipes() == -1){
            recipes = recipeService.getRecipesByGroceriesAndExpirationDates(fetchRecipesDTO, true);
        }
        else{
            recipes = recipeService.getRecipesByGroceriesAndExpirationDates(fetchRecipesDTO, false);
        }*/
        recipes = recipeService.getSortedRecipesByMatchingGroceries(fetchRecipesDTO.getRefrigeratorId(), fetchRecipesDTO.getNumRecipes());

        return ResponseEntity.ok(recipes);
    }

    /**
     * Getter for all recipes
     * @return list with all recipes
     * @throws NoSuchElementException If no recipes
     */
    @Operation(summary = "Get all recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All recipes retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllRecipes() throws NoSuchElementException {
        logger.info("Received request to get all recipes");
        List<RecipeDTO> allRecipes = recipeService.getAllRecipes();
        if (allRecipes.isEmpty()) {
            throw new NoSuchElementException("No recipes found");
        }

        return ResponseEntity.ok(allRecipes);
    }
}
