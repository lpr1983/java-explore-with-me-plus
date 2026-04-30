package ewm.main.category.repository;

import ewm.main.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    @Query("SELECT COUNT(c) FROM Category c WHERE NOT c.id = :id AND c.name = :name")
    long countByNameExcludingId(@Param("id") Long id, @Param("name") String name);
}
