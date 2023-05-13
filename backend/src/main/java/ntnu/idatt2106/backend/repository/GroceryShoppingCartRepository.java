package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



/**
 * Repository for GroceryShoppingCart entity.
 */
@Repository
public interface GroceryShoppingCartRepository extends JpaRepository<GroceryShoppingCart, Long> {
    /**
     * Returns a optional GroceryShoppingCart element based on grocery id and shopping cart id.
     * @param groceryId
     * @param shoppingCartId
     * @return
     */
    Optional<GroceryShoppingCart> findByGroceryIdAndShoppingCartId(Long groceryId, Long shoppingCartId);
}
