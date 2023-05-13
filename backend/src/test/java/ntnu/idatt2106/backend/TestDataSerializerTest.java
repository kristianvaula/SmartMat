package ntnu.idatt2106.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import ntnu.idatt2106.backend.config.TestDataSerializer;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import ntnu.idatt2106.backend.repository.UnitRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import ntnu.idatt2106.backend.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestDataSerializerTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private GroceryRepository groceryRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private RecipeCategoryRepository recipeCategoryRepository;

    @InjectMocks
    private TestDataSerializer testDataSerializer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetRecipeByName() {
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        when(recipeRepository.findByName("Pasta")).thenReturn(Optional.of(recipe));

        Recipe result = testDataSerializer.getRecipeByName("Pasta");

        assertEquals(recipe, result);
        verify(recipeRepository).findByName("Pasta");
    }

    @Test
    void testGetGroceryById() {
        Grocery grocery = new Grocery();
        grocery.setId(1L);
        when(groceryRepository.findById(1L)).thenReturn(Optional.of(grocery));

        Grocery result = testDataSerializer.getGroceryById(1L);

        assertEquals(grocery, result);
        verify(groceryRepository).findById(1L);
    }

    @Test
    void testGetUnitById() {
        Unit unit = new Unit();
        unit.setId(1L);
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));

        Unit result = testDataSerializer.getUnitById(1L);

        assertEquals(unit, result);
        verify(unitRepository).findById(1L);
    }

    @Test
    void testGetRecipeCategoryByName() {
        RecipeCategory category = new RecipeCategory();
        category.setName("Italian");
        when(recipeCategoryRepository.findByName("Italian")).thenReturn(Optional.of(category));

        RecipeCategory result = testDataSerializer.getRecipeCategoryByName("Italian");

        assertEquals(category, result);
        verify(recipeCategoryRepository).findByName("Italian");
    }

    @Test
    void testGetRecipeCategoryByNameThrowsException() {
        String name = "Non-existent category";
        when(recipeCategoryRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> testDataSerializer.getRecipeCategoryByName(name));
        verify(recipeCategoryRepository).findByName(name);
    }
    }
