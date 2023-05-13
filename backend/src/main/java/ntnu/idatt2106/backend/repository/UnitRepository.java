package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for unit
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    /**
     * Finds a unit by it's ID
     * @param aLong ID to the unit
     * @return Optional of unit
     */
    @Override
    Optional<Unit> findById(Long aLong);
}
