package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for sub category
 */
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{
    /**
     * Find a sub category by it's ID
     * @param aLong ID to the sub category
     * @return Optional of sub category
     */
    @Override
    Optional<SubCategory> findById(Long aLong);

    /**
     * Finds a sub category by category name
     * @param name category name
     * @return Optional of sub category
     */
    Optional<SubCategory> findSubCategoryByName(String name);
}

