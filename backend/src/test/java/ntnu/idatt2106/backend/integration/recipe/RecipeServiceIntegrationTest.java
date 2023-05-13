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
public class RecipeServiceIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;
    private static HttpHeaders headers;
    private static HttpEntity<?> entity;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;


    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Autowired
    private RecipeCategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;


    @BeforeAll
    public static void setUpTestData(@Autowired TestRestTemplate restTemplate, @Autowired RecipeCategoryRepository recipeCategoryRepository) {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("create_new_fridge_test@email.com")
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
                "create_new_fridge_test@email.com",
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
                .id(1)
                .name("Egg")
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

        assertThat(newGroceryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newGroceryResponse.getBody().getId()).isEqualTo(1);
        assertThat(newGroceryResponse.getBody().getName()).isEqualTo("Egg");
        assertThat(newGroceryResponse.getBody().getGroceryExpiryDays()).isEqualTo(7);

        createRecipeCategories(recipeCategoryRepository);

    }

    @BeforeEach
    public void setup(){
        recipeRepository.deleteAll();
        refrigeratorGroceryRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
    }



    private static void createRecipeCategories(RecipeCategoryRepository recipeCategoryRepository) {
        String[] categories = {"Breakfast", "Lunch", "Dinner", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            recipeCategoryRepository.save(RecipeCategory.builder()
                    .name(category).build());
        });
    }

}
