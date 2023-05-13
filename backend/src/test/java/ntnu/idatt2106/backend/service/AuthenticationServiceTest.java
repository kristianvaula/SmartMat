package ntnu.idatt2106.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ntnu.idatt2106.backend.exceptions.UserAlreadyExistsException;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.model.enums.UserRole;
import ntnu.idatt2106.backend.repository.UserRepository;
import ntnu.idatt2106.backend.service.AuthenticationService;
import org.apache.http.auth.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(repository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegister() throws UserAlreadyExistsException {

        RegisterRequest request = new RegisterRequest("test@example.com", "Test User", "password123");
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .favoriteRefrigeratorId(-1L)
                .build();
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(user);


        RegisterResponse response = authenticationService.register(request);


        assertEquals(user.getId(), response.getUserId());
        assertEquals(String.valueOf(user.getUserRole()), response.getUserRole());
        assertEquals("User registered successfully!", response.getMessage());
    }

    @Test
    void testRegisterUserAlreadyExists() {

        RegisterRequest request = new RegisterRequest("test@example.com", "Test User", "password123");
        User existingUser = User.builder().email(request.getEmail()).build();
        when(repository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));


        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(request));
    }

    @Test
    void testAuthenticate() throws InvalidCredentialsException {

        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password123");
        User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).build();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(repository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token123");


        AuthenticationResponse response = authenticationService.authenticate(request);


        assertEquals("token123", response.getToken());
    }
}