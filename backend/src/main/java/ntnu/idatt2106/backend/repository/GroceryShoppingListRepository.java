package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for GroceryShoppingList entity.
 */
@Repository
public interface GroceryShoppingListRepository extends JpaRepository<GroceryShoppingList, Long> {

    /**
     * Returns a optional GroceryShoppingList based on a groceryid and shopping list id.
     * @param groceryId
     * @param shoppingListId
     * @return
     */
    Optional<GroceryShoppingList> findByGroceryIdAndShoppingListId(Long groceryId, Long shoppingListId);

    /**
     * Queries the database for a list of GroceryShoppingLists based on a shoppinglist id and isRequested bool.
     * @param shoppingListId
     * @param isRequested
     * @return
     */
    @Query(value = "SELECT gsl" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId AND gsl.isRequest = :isRequested")
    List<GroceryShoppingList> findByShoppingListId(@Param("shoppingListId")Long shoppingListId,
                                                   @Param("isRequested")boolean isRequested);


    /**
     * Queries the database for a list of GroceryShoppingLists based on a shoppinglist id, category id and isRequested bool.
     * @param shoppingListId
     * @param isRequested
     * @param categoryId
     * @return
     */
    @Query(value = "SELECT gsl" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId AND g.subCategory.category.id = :categoryId AND gsl.isRequest = :isRequested")
    List<GroceryShoppingList> findByShoppingListIdAndCategoryId(@Param("shoppingListId")Long shoppingListId,
                                                                @Param("categoryId")Long categoryId,
                                                                @Param("isRequested")boolean isRequested);

    /**
     * Queries the database for a list of categories in a shopping list, based on a shopping list id.
     * @param shoppingListId
     * @return
     */
    @Query(value = "SELECT DISTINCT sc.category" +
            " FROM GroceryShoppingList gsl, Grocery g, SubCategory sc" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId AND sc.id = g.subCategory.id")
    List<Category> findCategoryByShoppingListId(@Param("shoppingListId")Long shoppingListId);
}

