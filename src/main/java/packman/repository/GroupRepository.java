package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
