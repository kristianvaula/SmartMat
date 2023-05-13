package ntnu.idatt2106.backend.model.dto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ntnu.idatt2106.backend.model.dto.recipe.IngredientDTO;
import ntnu.idatt2106.backend.model.dto.recipe.SimpleGrocery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2106.backend.model.Unit;

public class IngredientDTOTest {

    private IngredientDTO ingredient;

    @BeforeEach
    void setUp() {
        SimpleGrocery grocery = new SimpleGrocery();
        Unit unit = Unit.builder().name("1").weight(100).id(1L).build();
        int quantity = 200;

        ingredient = new IngredientDTO(grocery, quantity, unit);
    }

    @Test
    void testGettersAndSetters() {
        SimpleGrocery newGrocery = new SimpleGrocery();
        Unit unit = Unit.builder().name("1").weight(100).id(1L).build();
        int newQuantity = 500;

        ingredient.setSimpleGrocery(newGrocery);
        ingredient.setQuantity(newQuantity);
        ingredient.setUnit(unit);

        assertEquals(newGrocery, ingredient.getSimpleGrocery());
        assertEquals(newQuantity, ingredient.getQuantity());
        assertEquals(unit, ingredient.getUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        SimpleGrocery grocery = new SimpleGrocery();
        Unit unit = Unit.builder().name("1").weight(100).id(1L).build();
        int quantity = 200;

        IngredientDTO sameIngredient = new IngredientDTO(grocery, quantity, unit);
        IngredientDTO differentIngredient = new IngredientDTO(grocery, 100, unit);

        // Test equals()
        assertEquals(ingredient, sameIngredient);
        assertNotEquals(ingredient, differentIngredient);

        // Test hashCode()
        assertEquals(ingredient.hashCode(), sameIngredient.hashCode());
        assertNotEquals(ingredient.hashCode(), differentIngredient.hashCode());
    }
}