package ntnu.idatt2106.backend.integration;

import jakarta.transaction.Transactional;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    @Transactional
    public void setUpTestData() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("login@email.com")
                .name("Test User")
                .password("testpassword")
                .build();

        ResponseEntity<RegisterResponse> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                registerRequest,
                RegisterResponse.class
        );

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void authenticateUser() {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                "login@email.com",
                "testpassword"
        );

        ResponseEntity<AuthenticationResponse> authenticationResponse = restTemplate.postForEntity(
                "/api/auth/login",
                authenticationRequest,
                AuthenticationResponse.class
        );

        System.out.println(authenticationResponse);

        assertThat(authenticationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authenticationResponse.getBody().getUserId()).isNotEmpty();
        assertThat(authenticationResponse.getBody().getUserRole()).isNotEmpty();
    }
}