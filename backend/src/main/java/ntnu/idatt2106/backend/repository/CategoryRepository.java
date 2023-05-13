package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Returns an optional category based on id.
     * @param aLong
     * @return
     */
    @Override
    Optional<Category> findById(Long aLong);

    /**
     * Returns an optional category based on name.
     * @param name
     * @return
     */
    Optional<Category> findCategoryByName(String name);
}
