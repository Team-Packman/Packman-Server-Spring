package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
