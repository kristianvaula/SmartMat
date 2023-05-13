package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for refrigerator users
 */
public interface RefrigeratorUserRepository extends JpaRepository<RefrigeratorUser, Long> {
    /**
     * Finds refrigerator user bu user ID and refrigerator ID
     * @param userId ID of user
     * @param refrigeratorId ID of refrigerator
     * @return Optional of refrigerator user
     */
    Optional<RefrigeratorUser> findByUser_IdAndRefrigerator_Id(String userId, long refrigeratorId);

    /**
     * Finds refrigerator user bu user and refrigerator
     * @param user User
     * @param refrigerator Refrigerator
     * @return Optional of refrigerator user
     */
    Optional<RefrigeratorUser> findByUserAndRefrigerator(User user, Refrigerator refrigerator);

    /**
     * Finds a list of refrigerator user by refrigerator ID and fridge role
     * @param refrigeratorId ID to the refrigerator
     * @param fridgeRole Role in refrigerator
     * @return List of user in the refrigerator with the specified role
     */
    List<RefrigeratorUser> findByRefrigeratorIdAndFridgeRole(long refrigeratorId, FridgeRole fridgeRole);

    /**
     * Finds a list of refrigerator user by refrigerator id
     * @param refrigeratorId ID to refrigerator
     * @return List of refrigerator users in the given refrigerator
     */
    List<RefrigeratorUser> findByRefrigeratorId(long refrigeratorId);

    /**
     * Finds a list of refrigerator user for a given user
     * @param user User
     * @return List of refrigerator users for a given user
     */
    List<RefrigeratorUser> findByUser(User user);

    /**
     * Remove all refrigerator users with a given refrigerator ID
     * @param refrigeratorId ID of refrigerator
     */
    void removeByRefrigeratorId(long refrigeratorId);
}
