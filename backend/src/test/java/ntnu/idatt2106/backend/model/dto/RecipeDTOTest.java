package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeDTOTest {

    private RecipeDTO recipeDTO1;
    private RecipeDTO recipeDTO2;
    private RecipeDTO recipeDTO3;

    @BeforeEach
    void setUp() {
        recipeDTO1 = new RecipeDTO(1L, "Recipe1", "www.example.com/recipe1", new ArrayList<>());
        recipeDTO2 = new RecipeDTO(2L, "Recipe2", "www.example.com/recipe2", new ArrayList<>());
        recipeDTO3 = new RecipeDTO(1L, "Recipe1", "www.example.com/recipe1", new ArrayList<>());
    }

    @Test
    void testEquals() {
        assertEquals(recipeDTO1, recipeDTO1); // reflexive
        assertEquals(recipeDTO1, recipeDTO3); // symmetric
        assertNotEquals(recipeDTO1, recipeDTO2); // different ids
        assertNotEquals(recipeDTO1, null); // null check
        assertNotEquals(recipeDTO1, "not a recipeDTO"); // type check
    }

    @Test
    void testHashCode() {
        assertEquals(recipeDTO1.hashCode(), recipeDTO3.hashCode());
        assertNotEquals(recipeDTO1.hashCode(), recipeDTO2.hashCode());
    }

}