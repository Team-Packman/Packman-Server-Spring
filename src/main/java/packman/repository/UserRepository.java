package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeleted(Long userId, boolean isDeleted);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndRefreshToken(Long userId, String refreshToken);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.isDeleted = true where u.id = :userId")
    void setUserIsDeletedByUserId(@Param("userId") Long userId);
}
