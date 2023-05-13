package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.dto.recipe.FetchRecipesDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class FetchRecipeDTOTest {
    @Test
    public void testGetAndSetRefrigeratorId() {
        FetchRecipesDTO fetchRecipesDTO = new FetchRecipesDTO();
        fetchRecipesDTO.setRefrigeratorId(123L);
        Assertions.assertEquals(123L, fetchRecipesDTO.getRefrigeratorId());
    }

    @Test
    public void testGetAndSetNumRecipes() {
        FetchRecipesDTO fetchRecipesDTO = new FetchRecipesDTO();
        fetchRecipesDTO.setNumRecipes(5);
        Assertions.assertEquals(5, fetchRecipesDTO.getNumRecipes());
    }

    @Test
    public void testGetAndSetFetchedRecipeIds() {
        FetchRecipesDTO fetchRecipesDTO = new FetchRecipesDTO();
        List<Long> fetchedRecipeIds = Arrays.asList(1L, 2L, 3L);
        fetchRecipesDTO.setFetchedRecipeIds(fetchedRecipeIds);
        Assertions.assertEquals(fetchedRecipeIds, fetchRecipesDTO.getFetchedRecipeIds());
    }
    @Test
    public void testEqualsAndHashCode() {
        FetchRecipesDTO fetchRecipesDTO1 = new FetchRecipesDTO();
        fetchRecipesDTO1.setRefrigeratorId(123L);
        fetchRecipesDTO1.setNumRecipes(5);
        List<Long> fetchedRecipeIds1 = Arrays.asList(1L, 2L, 3L);
        fetchRecipesDTO1.setFetchedRecipeIds(fetchedRecipeIds1);

        FetchRecipesDTO fetchRecipesDTO2 = new FetchRecipesDTO();
        fetchRecipesDTO2.setRefrigeratorId(123L);
        fetchRecipesDTO2.setNumRecipes(5);
        List<Long> fetchedRecipeIds2 = Arrays.asList(1L, 2L, 3L);
        fetchRecipesDTO2.setFetchedRecipeIds(fetchedRecipeIds2);

        FetchRecipesDTO fetchRecipesDTO3 = new FetchRecipesDTO();
        fetchRecipesDTO3.setRefrigeratorId(456L);
        fetchRecipesDTO3.setNumRecipes(5);
        List<Long> fetchedRecipeIds3 = Arrays.asList(1L, 2L, 3L);
        fetchRecipesDTO3.setFetchedRecipeIds(fetchedRecipeIds3);

        Assertions.assertEquals(fetchRecipesDTO1, fetchRecipesDTO2);
        Assertions.assertEquals(fetchRecipesDTO1.hashCode(), fetchRecipesDTO2.hashCode());

        Assertions.assertNotEquals(fetchRecipesDTO1, fetchRecipesDTO3);
        Assertions.assertNotEquals(fetchRecipesDTO1.hashCode(), fetchRecipesDTO3.hashCode());
    }
}
