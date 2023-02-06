package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Group;
import packman.entity.UserGroup;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByGroupAndUserId(Group group, Long userId);
    Optional<UserGroup> findByGroupAndUserIdAndUser_IsDeleted(Group group, Long userId, boolean isDeleted);
}
