package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents the scenario where an operation
 * cannot be completed because it would leave the system without any superusers.
 * <p>
 * This exception is thrown when attempting to perform an action that would
 * result in having no superusers available, e.g., deleting the last superuser
 * or demoting the last superuser to a regular user.
 * </p>
 */
public class LastSuperuserException extends Exception {
    /**
     * Constructs a new LastSuperuserException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method.
     */
    public LastSuperuserException(String message) {
        super(message);
    }
}
