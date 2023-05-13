package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for user
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds user by user e-mail
     * @param email to user
     * @return Optional of user
     */
    Optional<User> findByEmail(String email);

    /**
     * The methods control if it exists a user by the e-mail in the parameter
     * @param email the email to control
     * @return true if it exists a user by the given e-mail
     */
    boolean existsByEmail(String email);
}
