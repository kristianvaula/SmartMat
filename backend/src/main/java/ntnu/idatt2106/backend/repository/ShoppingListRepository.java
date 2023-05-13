package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for shoppig list
 */
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    /**
     * Finds a shopping list by it's ID
     * @param id ID of shopping list
     * @return Optional of shopping list
     */
    Optional<ShoppingList> findById(Long id);

    /**
     * Finds a shopping list by refrigerator id
     * @param id ID to refrigerator
     * @return Optional of shopping list
     */
    @Query("SELECT s FROM ShoppingList s WHERE s.refrigerator.id = :refrigeratorId")
    Optional<ShoppingList> findByRefrigeratorId(@Param(value = "refrigeratorId") Long id);

    /**
     * Finds the refrigerator assosiated shopping list id
     * @param id ID to shopping list
     * @return Refrigerator object for the shopping list
     */
    @Query(value = "SELECT s.refrigerator FROM ShoppingList s WHERE s.id = :id")
    Refrigerator findRefrigeratorById(Long id);

    /**
     * Removes a shopping list by refrigerator ID
     * @param refrigeratorId ID to refrigerator
     */
    void removeByRefrigerator_Id(long refrigeratorId);
}
