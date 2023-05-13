package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for Refrigerator entity.
 */
@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    /**
     * Returns a optional refrigerator based on id.
     * @param id
     * @return
     */
    Optional<Refrigerator> findById(Long id);

    /**
     * boolean based on if refrigerator name exists.
     * @param name
     * @return
     */
    boolean existsByName(String name);

    /**
     * Returns optional refrigerator based on name.
     * @param test_refrigerator
     * @return
     */
    Optional<Refrigerator> findByName(String test_refrigerator);

}
