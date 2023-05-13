package ntnu.idatt2106.backend.unit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ntnu.idatt2106.backend.controller.AuthenticationController;
import ntnu.idatt2106.backend.exceptions.UserAlreadyExistsException;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.model.enums.UserRole;
import ntnu.idatt2106.backend.service.AuthenticationService;
import ntnu.idatt2106.backend.service.UserService;
import org.apache.http.auth.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AuthenticationTests {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController loginController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private User user;

    @Test
    public void register_returnsOkResponse() throws UserAlreadyExistsException {
        RegisterRequest registerRequest = RegisterRequest.builder().email("email").name("name").password("password").build();
        RegisterResponse registerResponse = RegisterResponse.builder().build();

        System.out.println(registerResponse);

        when(authenticationService.register(registerRequest)).thenReturn(registerResponse);

        ResponseEntity<RegisterResponse> response = loginController.register(registerRequest);

        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registerResponse, response.getBody());
    }

    @Test
    public void register_throwsUserAlreadyExistsException() throws UserAlreadyExistsException {
        RegisterRequest registerRequest = new RegisterRequest();

        when(authenticationService.register(registerRequest)).thenThrow(UserAlreadyExistsException.class);

        assertThrows(UserAlreadyExistsException.class, () -> {
            loginController.register(registerRequest);
        });
    }

    @Test
    public void authenticate_returnsOkResponse() throws InvalidCredentialsException, IOException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().token("token").build();

        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        // Set up the mock to return a non-null User object
        when(userService.findByEmail(authenticationRequest.getEmail())).thenReturn(user);
        when(user.getId()).thenReturn(String.valueOf(UUID.randomUUID()));
        when(httpServletRequest.getServerName()).thenReturn("localhost");


        // Set up the mock to return a non-null Role object
        when(user.getUserRole()).thenReturn(UserRole.USER); // Replace Role.USER with the appropriate enum constant

        ResponseEntity<AuthenticationResponse> response = loginController.authenticate(authenticationRequest, httpServletResponse, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, response.getBody());
    }



    @Test
    public void authenticate_throwsInvalidCredentialsException() throws InvalidCredentialsException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        when(authenticationService.authenticate(authenticationRequest)).thenThrow(InvalidCredentialsException.class);

        assertThrows(InvalidCredentialsException.class, () -> {
            loginController.authenticate(authenticationRequest, null, httpServletRequest);
        });
    }

}
