package ntnu.idatt2106.backend;

import ntnu.idatt2106.backend.exceptions.*;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the backend API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * This method handles a FileSizeLimitExceededException and returns a ResponseEntity with an HTTP status of
     * BAD_REQUEST and a message indicating that the file size limit has been exceeded.
     * @param ex The exception that was thrown
     * @return A ResponseEntity with an HTTP status of BAD_REQUEST and a message indicating that the file size limit
     *         has been exceeded
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<String> handleFileSizeLimitExceededException(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("The file size limit has been exceeded: " + ex.getMessage());
    }

    /**
     * This method handles a UsernameNotFoundException and returns a ResponseEntity with an HTTP status of NO_CONTENT
     * and a message indicating that the requested username was not found in the database.
     * @param ex The exception that was thrown
     * @return A ResponseEntity with an HTTP status of NO_CONTENT and a message indicating that the requested username
     *         was not found in the database
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Username was not found in database" + ex.getMessage());
    }

    /**
     * This method handles a UserAlreadyExistsException and returns a ResponseEntity with an HTTP status of BAD_REQUEST
     * and the message that was thrown by the exception.
     * @param ex The exception that was thrown
     * @return A ResponseEntity with an HTTP status of BAD_REQUEST and the message that was thrown by the exception
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Handles InvalidCredentialsException and returns a 401 Unauthorized HTTP response
     * @param ex The InvalidCredentialsException that was thrown
     * @return ResponseEntity with a status of 401 Unauthorized and a message containing the error message from the exception
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Handles UnauthorizedException and returns a 401 Unauthorized HTTP response
     * @param ex The UnauthorizedException that was thrown
     * @return ResponseEntity with a status of 401 Unauthorized and a message containing the error message from the exception
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Handles NullPointerException and returns a 204 No Content HTTP response
     * @param ex The NullPointerException that was thrown
     * @return ResponseEntity with a status of 204 No Content and a message containing the error message from the exception
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Exception handler for ShoppingListNotFound exception
     * @param ex the exception that was thrown
     * @return ResponseEntity containing a NO_CONTENT status and an error message
     */
    @ExceptionHandler(ShoppingListNotFound.class)
    public ResponseEntity<String> handleShoppingListNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Exception handler for ShoppingCartNotFound exception
     * @param ex the exception that was thrown
     * @return ResponseEntity containing a NO_CONTENT status and an error message
     */
    @ExceptionHandler(ShoppingCartNotFound.class)
    public ResponseEntity<String> handleShoppingCartNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Exception handler for SubCategoryNotFound exception
     * @param ex the exception that was thrown
     * @return ResponseEntity containing a NO_CONTENT status and an error message
     */
    @ExceptionHandler(SubCategoryNotFound.class)
    public ResponseEntity<String> handleSubCategoryNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * The exception handlers for CategoryNotFound
     * @param ex the exception that was thrown
     * @return ResponseEntity containing a NO_CONTENT status and an error message
     */
    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<String> handleCategoryNotFound(Exception ex) {
        logger.info("Could not find any categories");
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Handles SaveException by returning a ResponseEntity with an INTERNAL_SERVER_ERROR status and the exception message.
     * @param ex the SaveException that was thrown
     * @return ResponseEntity containing an INTERNAL_SERVER_ERROR status and an error message
     */
    @ExceptionHandler(SaveException.class)
    public ResponseEntity<String> handleSaveException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    /**
     * Handles AccessDeniedException by returning a ResponseEntity with an UNAUTHORIZED status and the exception message.
     * @param ex the AccessDeniedException that was thrown
     * @return ResponseEntity containing an UNAUTHORIZED status and an error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Handles NoSuchElementException by returning a ResponseEntity with a NO_CONTENT status and the exception message.
     * @param ex the NoSuchElementException that was thrown
     * @return ResponseEntity containing a NO_CONTENT status and an error message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Handles RefrigeratorNotFoundException and returns a ResponseEntity with a NOT_FOUND status code
     * and the exception message as the response body.
     * @param ex the RefrigeratorNotFoundException that was thrown
     * @return ResponseEntity containing the exception message and a NOT_FOUND status code
     */
    @ExceptionHandler(RefrigeratorNotFoundException.class)
    public ResponseEntity<String> handleRefrigeratorNotFoundException(RefrigeratorNotFoundException ex) {
        logger.warn("RefrigeratorNotFoundException thrown: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserNotFoundException and returns a ResponseEntity with a NO_CONTENT status code
     * and the exception message as the response body.
     * @param ex the UserNotFoundException that was thrown
     * @return ResponseEntity containing the exception message and a NO_CONTENT status code
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("RefrigeratorNotFoundException thrown: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }

    /**
     * Handles NoGroceriesFound and returns a ResponseEntity with a NO_CONTENT status code
     * and the exception message as the response body.
     * @param ex the NoGroceriesFound that was thrown
     * @return ResponseEntity containing the exception message and a NO_CONTENT status code
     */
    @ExceptionHandler(NoGroceriesFound.class)
    public ResponseEntity<String> handleNoGroceryFound(NoGroceriesFound ex) {
        logger.warn("NoGroceryFound thrown: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }

    /**
     * Handles exceptions related to an old password not matching the current password.
     * @param ex the exception that was thrown
     * @return ResponseEntity containing a BAD_REQUEST status and an error message
     */
    @ExceptionHandler(OldPasswordDoesNotMatchException.class)
    public ResponseEntity<String> handleOldPasswordDoesNotMatchException(OldPasswordDoesNotMatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Handles general exceptions that were not explicitly handled by other exception handlers.
     * @param ex the exception that was thrown
     * @return ResponseEntity containing an INTERNAL_SERVER_ERROR status and an error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.info("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    /**
     * Handles exceptions related to attempting to remove the last superuser from a refrigerator.
     * @param e the exception that was thrown
     * @return ResponseEntity containing an OK status and a warning message
     */
    @ExceptionHandler(LastSuperuserException.class)
    public ResponseEntity<String> handleLastSuperUserexception(LastSuperuserException e){
        logger.warn("Could not complete operation: Is last superuser in refrigerator");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
    * Exception handler method for {@link NotificationException}.
    * @param ex the NotificationException that was thrown
    * @return a ResponseEntity with a INTERNAL_SERVER_ERROR status and an error message
    */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<String> handleNotificationException(NotificationException ex){
        logger.info("An error uccured when retrieving notifications" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred when retrieving notifications: " + ex.getMessage());
    }
}
