package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeleted(Long userId, boolean isDeleted);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndRefreshToken(Long userId, String refreshToken);
}
