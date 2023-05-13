package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for shopping cart
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    /**
     * Finds all grocery in shopping cart from shopping cart ID
     * @param shoppingCartId ID to shopping cart
     * @return List of all groceries from shopping cart ID
     */
    @Query(value = "SELECT gsc" +
            " FROM ShoppingCart sc, GroceryShoppingCart gsc, Grocery g" +
            " WHERE gsc.grocery.id = g.id AND sc.id = gsc.shoppingCart.id AND sc.id = :shoppingCartId")
    List<GroceryShoppingCart> findByShoppingCartId(@Param("shoppingCartId")Long shoppingCartId);

    /**
     * Finds shopping cart assosiated with shopping list id
     * @param id ID to shopping list
     * @return Optional of shopping cart
     */
    Optional<ShoppingCart> findByShoppingListId(Long id);

    /**
     * Remove shopping cart by shopping list
     * @param shoppingList Shopping list assosiated with the shopping cart
     */
    void removeByShoppingList(ShoppingList shoppingList);
}
