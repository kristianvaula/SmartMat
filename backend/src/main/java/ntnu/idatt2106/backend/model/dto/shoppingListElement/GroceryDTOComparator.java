package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import java.util.Comparator;

/**
 * Comparator for grocery DTO
 * The comparator sort grocery in alphabetical order
 */
public class GroceryDTOComparator implements Comparator<GroceryDTO>{

    /**
     * Compares to grocery dto on their description
     * @param g1 the first object to be compared.
     * @param g2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the description of g1 is less than, equal to, or greater than the description of g2, respectively
     */
    @Override
    public int compare(GroceryDTO g1, GroceryDTO g2) {
        return g1.getDescription().compareTo(g2.getDescription());
    }
}
