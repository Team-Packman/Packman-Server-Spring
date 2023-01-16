package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
