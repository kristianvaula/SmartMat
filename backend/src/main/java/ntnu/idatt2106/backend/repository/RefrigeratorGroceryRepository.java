package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for RefrigeratorGrocery entity.
 */
@Repository
public interface RefrigeratorGroceryRepository extends JpaRepository<RefrigeratorGrocery, Long> {

    /**
     * Returns a optional refrigeratorGrocery based on the id.
     * @param aLong
     * @return
     */
    @Override
    Optional<RefrigeratorGrocery> findById(Long aLong);

    /**
     * Returns all refrigeratorGrocery entities based on the refrigerator id.
     * @param aLong
     * @return
     */
    List<RefrigeratorGrocery> findAllByRefrigeratorId(Long aLong);

    /**
     * Removes all refrigeratorGroceries in a refrigerator based on refrigerator id.
     * @param refrigeratorId
     */

    void removeByRefrigeratorId(long refrigeratorId);


    /**
     * Returns a boolean whether or not a grocery exists in a refrigerator.
     * @param refrigerator
     * @param grocery
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(rg) > 0 THEN true ELSE false END FROM RefrigeratorGrocery rg WHERE rg.refrigerator = :refrigerator AND rg.grocery = :grocery")
    boolean existsByRefrigeratorAndGrocery(@Param("refrigerator") Refrigerator refrigerator, @Param("grocery") Grocery grocery);
}
