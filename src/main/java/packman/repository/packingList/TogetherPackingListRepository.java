package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.entity.User;
import packman.entity.Group;
import packman.entity.packingList.TogetherPackingList;

import java.util.List;
import java.util.Optional;

@Repository
public interface TogetherPackingListRepository extends JpaRepository<TogetherPackingList, Long> {
    boolean existsByInviteCode(String inviteCode);
    Optional<TogetherPackingList> findByInviteCode(String inviteCode);
    Optional<TogetherPackingList> findByIdAndPackingList_IsDeletedAndGroup_UserGroups_User(Long Id, boolean isDeleted, User user);
    @Modifying(clearAutomatically = true)
    @Query("update TogetherPackingList tp set tp.group = null where tp.group in :groups")
    void updateGroupNull(@Param("groups") List<Group> groups);
}
