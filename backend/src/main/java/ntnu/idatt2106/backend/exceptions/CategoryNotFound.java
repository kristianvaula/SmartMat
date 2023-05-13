package ntnu.idatt2106.backend.exceptions;

/**
 * Exception for when it does not exist a category
 */
public class CategoryNotFound extends Exception {
    public  CategoryNotFound (String message) {
        super(message);
    }
}
