package ntnu.idatt2106.backend.integration.recipe;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.repository.*;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeCategoryTest{


    @Autowired
    private TestRestTemplate restTemplate;
    private static HttpHeaders headers;
    private static HttpEntity<?> entity;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() {
        restTemplate = new TestRestTemplate();
        recipeRepository.deleteAll();
        recipeRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
    }

    @Test
    public void createRecipeCategories(@Autowired RecipeCategoryRepository recipeCategoryRepository) {
        String[] categories = {"Breakfast", "Lunch", "Dinner", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            recipeCategoryRepository.save(RecipeCategory.builder()
                    .name(category).build());
        });

        testRecipeCategoriesExist();
    }

    public void testRecipeCategoriesExist() {
        List<String> expectedCategories = Arrays.asList("Breakfast", "Lunch", "Dinner", "Dessert", "Snack");
        List<String> actualCategories = recipeCategoryRepository.findAll()
                .stream()
                .map(RecipeCategory::getName)
                .collect(Collectors.toList());
        assertThat(actualCategories).containsExactlyInAnyOrderElementsOf(expectedCategories);
    }

}
