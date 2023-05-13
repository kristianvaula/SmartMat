package ntnu.idatt2106.backend.model.dto.shoppingCartElement;


import java.util.Comparator;

/**
 * Comparator for shopping cart element DTO
 * The comparator sort groceries in shopping cart in alphabetical order
 */
public class ShoppingCartElementDTOComparator implements Comparator<ShoppingCartElementDTO> {


    /**
     * Compares two ShoppingCartElementDTO objects based on their description field.
     * @param s1 the first ShoppingCartElementDTO to be compared
     * @param s2 the second ShoppingCartElementDTO to be compared
     * @return an integer indicating the order of the two ShoppingCartElementDTO objects based on their description field
     */
    @Override
    public int compare(ShoppingCartElementDTO s1, ShoppingCartElementDTO s2) {
        return s1.getDescription().compareTo(s2.getDescription());
    }
}
