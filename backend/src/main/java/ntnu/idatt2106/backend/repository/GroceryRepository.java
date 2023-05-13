package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.grocery.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for Grocery entity.
 */
@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Long>{
    /**
     * Returns a optional grocery based on id.
     * @param aLong
     * @return
     */
    @Override
    Optional<Grocery> findById(Long aLong);

    /**
     * Returns a optional grocery based on the name.
     * @param name
     * @return
     */
    Optional<Grocery> findByName(String name);

}
