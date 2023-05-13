package ntnu.idatt2106.backend.model.enums;

/**
 * Enum which holds the state of a users current authentication.
 */
public enum AuthenticationState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    TOKEN_EXPIRED
}
