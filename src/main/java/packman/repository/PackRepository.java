package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Pack;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
}
