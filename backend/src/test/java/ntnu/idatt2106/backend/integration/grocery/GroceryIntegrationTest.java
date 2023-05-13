package ntnu.idatt2106.backend.integration.grocery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.recipe.RecipeDTO;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.repository.*;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableTransactionManagement
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GroceryIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;
    private static HttpHeaders headers;


    @Autowired
    private GroceryRepository groceryRepository;

    @BeforeAll
    public static void setUpTestData(@Autowired TestRestTemplate restTemplate) {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("grocery_test@email.com")
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
                "grocery_test@email.com",
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
    }

    @Test
    public void createGrocery(){

        GroceryDTO groceryDTO = GroceryDTO.builder()
                .name("GroceryIntegrationTest")
                .groceryExpiryDays(7)
                .build();


        HttpEntity<GroceryDTO> request = new HttpEntity<>(groceryDTO, headers);
        ResponseEntity<Grocery> response = restTemplate.exchange(
                "/api/refrigerator/grocery/create",
                HttpMethod.POST,
                request,
                Grocery.class
        );

        Grocery insertedGrocery = groceryRepository.findByName("GroceryIntegrationTest").get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(insertedGrocery.getId());
        assertThat(response.getBody().getName()).isEqualTo(insertedGrocery.getName());
        assertThat(response.getBody().getGroceryExpiryDays()).isEqualTo(insertedGrocery.getGroceryExpiryDays());
    }
}
