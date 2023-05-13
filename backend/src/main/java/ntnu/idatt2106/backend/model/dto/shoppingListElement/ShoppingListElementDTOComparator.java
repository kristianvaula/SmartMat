package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import java.util.Comparator;

/**
 * Comparator for shopping list element DTO
 * The comparator sort groceries in shopping list in alphabetical order
 */
public class ShoppingListElementDTOComparator implements Comparator<ShoppingListElementDTO> {

    /**
     * Compares two ShoppingListElementDTO objects based on their description field.
     * @param s1 the first ShoppingListElementDTO to be compared
     * @param s2 the second ShoppingListElementDTO to be compared
     * @return an integer indicating the order of the two ShoppingListElementDTO objects based on their description field
     */
    @Override
    public int compare(ShoppingListElementDTO s1, ShoppingListElementDTO s2) {
        return s1.getDescription().compareTo(s2.getDescription());
    }
}
