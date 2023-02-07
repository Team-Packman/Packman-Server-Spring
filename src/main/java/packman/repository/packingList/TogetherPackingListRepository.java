package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.User;
import packman.entity.packingList.TogetherPackingList;

import java.util.Optional;

@Repository
public interface TogetherPackingListRepository extends JpaRepository<TogetherPackingList, Long> {
    boolean existsByInviteCode(String inviteCode);
    Optional<TogetherPackingList> findByInviteCode(String inviteCode);
    Optional<TogetherPackingList> findByIdAndPackingList_IsDeletedAndGroup_UserGroups_User(Long Id, boolean isDeleted, User user);
}
