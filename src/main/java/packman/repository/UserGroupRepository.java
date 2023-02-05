package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Group;
import packman.entity.UserGroup;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup findByUserIdAndGroup(Long userId, Group Group);

    List<UserGroup> findByGroup(Group group);
}
