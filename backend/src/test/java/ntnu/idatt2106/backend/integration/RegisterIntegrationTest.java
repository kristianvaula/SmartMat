package ntnu.idatt2106.backend.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.service.AuthenticationService;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import javax.sql.DataSource;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registerUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("register@email.com")
                .name("Test User")
                .password("testpassword")
                .build();

        ResponseEntity<RegisterResponse> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                registerRequest,
                RegisterResponse.class
        );


        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(registerResponse.getBody()).getUserId()).isNotEmpty();
        assertThat(Objects.requireNonNull(registerResponse.getBody()).getUserRole()).isNotEmpty();
    }
}