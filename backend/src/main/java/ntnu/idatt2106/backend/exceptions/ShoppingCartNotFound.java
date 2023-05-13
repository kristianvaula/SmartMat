package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when a shopping cart is not found.
 */
public class ShoppingCartNotFound extends Exception {
    public ShoppingCartNotFound(String message) {
        super(message);
    }
}