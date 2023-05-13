package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.grocery.GroceryNotification;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for GroceryNotification entity.
 */
@Repository
public interface GroceryNotificationRepository extends JpaRepository<GroceryNotification, Long> {
    /**
     * Returns a Optional notification based on the id.
     * @param aLong
     * @return
     */
    @Override
    Optional<GroceryNotification> findById(Long aLong);

    /**
     * Returns a list of notification based on a user id.
     * @param userId
     * @return
     */
    List<GroceryNotification> findAllByUserId(String userId);

    /**
     * Returns a list of notifications based on a grocery.
     * @param groceryEntity
     * @return
     */
    List<GroceryNotification> findAllByGroceryEntity(RefrigeratorGrocery groceryEntity);

}
