package ntnu.idatt2106.backend.integration.recipe;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.repository.*;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
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
@EnableTransactionManagement
@Transactional
public class RecipeGroceryIntegrationTest{


    @Autowired
    private TestRestTemplate restTemplate;
    private static HttpHeaders headers;
    private static HttpEntity<?> entity;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private GroceryRepository groceryRepository;

    @Autowired
    private RecipeGroceryRepository recipeGroceryRepository;


    @BeforeAll
    public static void setUpTestData(@Autowired TestRestTemplate restTemplate, @Autowired RecipeCategoryRepository recipeCategoryRepository, @Autowired RecipeRepository recipeRepository, @Autowired GroceryRepository groceryRepository) {
        recipeRepository.deleteAll();
        recipeRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("create_new_fridge5_test@email.com")
                .name("Test User")
                .password("testpassword")
                .build();

        ResponseEntity<RegisterResponse> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                registerRequest,
                RegisterResponse.class
        );

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                "create_new_fridge5_test@email.com",
                "testpassword"
        );

        ResponseEntity<AuthenticationResponse> authenticationResponse = restTemplate.postForEntity(
                "/api/auth/login",
                authenticationRequest,
                AuthenticationResponse.class
        );

        assertThat(authenticationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, authenticationResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(headers);

        // Create a new refrigerator
        NewRefrigeratorDTO newRefrigeratorDTO = NewRefrigeratorDTO.builder()
                .userEmail(registerRequest.getEmail())
                .name("Test Refrigerator")
                .address("Test Address")
                .build();

        HttpEntity<NewRefrigeratorDTO> newRefrigeratorRequest = new HttpEntity<>(newRefrigeratorDTO, headers);
        ResponseEntity<Refrigerator> newRefrigeratorResponse = restTemplate.exchange(
                "/api/refrigerator/new",
                HttpMethod.POST,
                newRefrigeratorRequest,
                Refrigerator.class
        );

        assertThat(newRefrigeratorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newRefrigeratorResponse.getBody().getName()).isEqualTo("Test Refrigerator");
        assertThat(newRefrigeratorResponse.getBody().getAddress()).isEqualTo("Test Address");

        GroceryDTO groceryDTO = GroceryDTO.builder()
                .name("RecipeGroceryIntegrationTest")
                .groceryExpiryDays(7)
                .build();

        // Create a new grocery and add it to the refrigerator
        HttpEntity<GroceryDTO> newGroceryRequest = new HttpEntity<>(groceryDTO, headers);
        ResponseEntity<GroceryDTO> newGroceryResponse = restTemplate.exchange(
                "/api/refrigerator/grocery/create",
                HttpMethod.POST,
                newGroceryRequest,
                GroceryDTO.class
        );

        Grocery grocery = groceryRepository.findByName("RecipeGroceryIntegrationTest")
                .orElseThrow(() -> new RuntimeException("Grocery not found"));



        assertThat(newGroceryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newGroceryResponse.getBody().getId()).isEqualTo(grocery.getId());
        assertThat(newGroceryResponse.getBody().getName()).isEqualTo(grocery.getName());
        assertThat(newGroceryResponse.getBody().getGroceryExpiryDays()).isEqualTo(grocery.getGroceryExpiryDays());

        createRecipeCategories(recipeCategoryRepository);

        // create recipe object
        Recipe recipe = Recipe.builder()
                .name("New Recipe")
                .url("https://example.com/new-recipe")
                .category(recipeCategoryRepository.findByName("Dinner5")
                        .orElseThrow(() -> new RuntimeException("Recipe category not found")))
                .build();


        // insert recipe into database
        Recipe createdRecipe = recipeRepository.save(recipe);

        // verify that recipe was created successfully
        assertThat(createdRecipe.getId()).isNotNull();
        assertThat(createdRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(createdRecipe.getUrl()).isEqualTo(recipe.getUrl());
        assertThat(createdRecipe.getCategory().getName()).isEqualTo(recipe.getCategory().getName());

    }

    //@Test
    void testCreateRecipeGroceries() {
        // create recipe object
        Recipe recipe = Recipe.builder()
                .name("Eggerøre")
                .url("https://example.com/eggerore")
                .category(recipeCategoryRepository.findByName("Breakfast")
                        .orElseThrow(() -> new RuntimeException("Recipe category not found")))
                .build();

        // insert recipe into database
        Recipe createdRecipe = recipeRepository.save(recipe);




        Grocery grocery = Grocery.builder()
                .name("RecipeGroceryIntegrationTestEgg1")
                .groceryExpiryDays(7)
                .build();

        // insert grocery into database
        Grocery createdGrocery = groceryRepository.save(grocery);



        Grocery groceryfound = groceryRepository.findByName("RecipeGroceryIntegrationTestEgg1").orElse(null);

        assertThat(groceryfound).isNotNull();
        assertThat(groceryfound.getName()).isEqualTo(grocery.getName());
        assertThat(groceryfound.getGroceryExpiryDays()).isEqualTo(grocery.getGroceryExpiryDays());


        // create recipe-grocery objects
        RecipeGrocery recipeGrocery = RecipeGrocery.builder()
                .recipe(createdRecipe)
                .grocery(grocery)
                .quantity(1)
                .build();

        // insert recipe-grocery objects into database
        RecipeGrocery savedRecipeGrocery = recipeGroceryRepository.save(recipeGrocery);

        System.out.println(recipeGroceryRepository.findAll());
        System.out.println(recipeGroceryRepository.findByRecipeId(savedRecipeGrocery.getId()));


        // retrieve recipe from database and check that the recipe-groceries were created successfully
        RecipeGrocery retrievedRecipe = recipeGroceryRepository.findById(savedRecipeGrocery.getId()).orElse(null);
        assertThat(retrievedRecipe).isNotNull();
        assertThat(retrievedRecipe.getRecipe().getName()).isEqualTo(createdRecipe.getName());
        assertThat(retrievedRecipe.getGrocery().getName()).isEqualTo(grocery.getName());
        assertThat(retrievedRecipe.getQuantity()).isEqualTo(recipeGrocery.getQuantity());

    }



    private static void createRecipeCategories(RecipeCategoryRepository recipeCategoryRepository) {
        String[] categories = {"Breakfast", "Lunch", "Dinner5", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            recipeCategoryRepository.save(RecipeCategory.builder()
                    .name(category).build());
        });
    }

}
