package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.GroceryHistory;
import ntnu.idatt2106.backend.model.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Repository for GroceryHistory entity.
 */
@Repository
public interface GroceryHistoryRepository extends JpaRepository<GroceryHistory, Long> {
    /**
     * Returns a optional groceryHistory based on an id.
     * @param l
     * @return
     */
    @Override
    Optional<GroceryHistory> findById(Long l);

    /**
     * Returns a list of GroceryHistory elements between two dates and in a specified refrigerator.
     * @param startDate
     * @param endDate
     * @param refrigeratorId
     * @return
     */
    List<GroceryHistory> findByDateConsumedBetweenAndRefrigeratorId(LocalDate startDate, LocalDate endDate, Long refrigeratorId);

    /**
     * Deletes all consumed before given date.
     * @param date
     */
    void deleteByDateConsumedBefore(LocalDate date);

    /**
     * Finds a potential GroceryHistory object based on the date consumed, weight in grams and the refrigerator.
     * @param date
     * @param weight
     * @param refrigerator
     * @return
     */
    Optional<GroceryHistory> findByDateConsumedAndWeightInGramsAndRefrigerator(LocalDate date, int weight, Refrigerator refrigerator);
}
