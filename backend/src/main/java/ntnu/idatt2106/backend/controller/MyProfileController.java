package ntnu.idatt2106.backend.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.OldPasswordDoesNotMatchException;
import ntnu.idatt2106.backend.model.dto.PasswordChangeDTO;
import ntnu.idatt2106.backend.model.dto.UserProfileDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.dto.response.UserStatusResponse;
import ntnu.idatt2106.backend.model.enums.AuthenticationState;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controller for my profile
 * The controller contains method to edit the profile and get the profile
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "My profile controller", description = "Controller to handle user login")
public class MyProfileController {
    private final JwtService jwtService;
    private final UserService userService;

    private final CookieService cookieService;
    private final PasswordEncoder passwordEncoder;

    Logger logger = Logger.getLogger(MyProfileController.class.getName());

    /**
     * Retrieves the profile of the authenticated user.
     *
     * @param request the HTTP request containing the JWT token in the cookie
     * @return ResponseEntity containing the user's profile information
     */
    @Operation(summary = "Gets the profile of an authenticated user",
            description = "Retrieves the profile information of the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If authenticated, the authenticated user's profile information.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Returns error when authentication fails",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileDTO.class)))
            })
    @GetMapping("/my-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) throws Exception {
        try{

            User user = userService.findByEmail(jwtService.extractUsername(cookieService.extractTokenFromCookie(request)));
            logger.info("Recieved request to get user profile on user: "+ user.getEmail() + ".");


            UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .favoriteRefrigeratorId(user.getFavoriteRefrigeratorId() != null ? user.getFavoriteRefrigeratorId() : null)
                    .build();

            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Retrieves the authentication status of the user.
     *
     * @param request the HTTP request containing the JWT token in the cookie
     * @return ResponseEntity with status code 200 if the user is authenticated, 401 if the user is not authenticated
     */
    @Operation(summary = "Gets the authentication status of a user",
            description = "Retrieves the authentication status of the user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When user is authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationState.class))),
                    @ApiResponse(responseCode = "401", description = "When authentication fails.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationState.class))),
            })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user-status")
    public ResponseEntity<?> getUserStatus( HttpServletRequest request) throws Exception {
        try {
            String jwt = cookieService.extractTokenFromCookie(request); // Extract the token from the cookie

            User user = userService.findByEmail(jwtService.extractUsername(jwt)); // Pass the JWT token instead of the request
            logger.info("Received request to get user status on user: "+ user.getEmail() + ".");

            //use builder to build a userStatusResponse
            UserStatusResponse userStatusResponse = UserStatusResponse.builder()
                    .userId(user.getId())
                    .role(user.getUserRole()
                            .toString())
                    .state(jwtService.getAuthenticationState(jwt, user))
                    .favoriteRefrigeratorId(Optional.ofNullable(user.getFavoriteRefrigeratorId()).orElse(null))
                    .build();

            return ResponseEntity.ok(userStatusResponse);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Handles a request to edit the user's name and email.
     *
     * @param userProfileDTO A DTO representing the user's updated name and email.
     *
     * @param request The HTTP request.
     *
     * @return A ResponseEntity containing either the updated UserProfileDTO if the request was successful, or an ErrorResponse if there was an error.
     */
    @Operation(summary = "Changes the details of a user.",
            description = "This method changes the email and/or name of a user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When password is changed successfully.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "401", description = "When user is not authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/my-profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editMyProfile(@RequestBody UserProfileDTO userProfileDTO, HttpServletRequest request) {
        logger.info("Received request to edit profile; name: " + userProfileDTO.getName() + ", email: " + userProfileDTO.getEmail() + "");

        try {
            User user = userService.findByEmail(jwtService.extractUsername(cookieService.extractTokenFromCookie(request)));
            user.setName(userProfileDTO.getName());
            user.setEmail(userProfileDTO.getEmail());
            userService.save(user);
            return ResponseEntity.ok(userProfileDTO);
        }
        catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Token expired"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Bad request"));
        }
    }


    /**

     Handles a request to change the user's password.

     @param passwordChangeDTO A DTO representing the user's old and new passwords.

     @param request The HTTP request.

     @return A ResponseEntity containing either a SuccessResponse if the password was changed successfully, or an ErrorResponse if there was an error.
     */
    @PostMapping("/my-profile/change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Changes the password of an authenticated user",
            description = "This endpoint changes the password of a user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When password is changed successfully.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "401", description = "When user is not authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO, HttpServletRequest request) throws OldPasswordDoesNotMatchException {

        String jwt = cookieService.extractTokenFromCookie(request);

        User user = userService.findByEmail(jwtService.extractUsername(jwt));

        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            logger.info("Received request to change password for user: " + jwtService.extractUsername(jwt) + " but old password does not match current password.");
            throw new OldPasswordDoesNotMatchException("Old password does not match current password.");
        }

        logger.info("Received request to change password for user: " + jwtService.extractUsername(jwt) + ". Password changed successfully.");
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userService.save(user);

        return ResponseEntity.ok(new SuccessResponse("Password changed successfully", 200));
    }
}
