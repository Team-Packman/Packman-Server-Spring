package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.entity.Group;
import packman.entity.Pack;
import packman.entity.packingList.TogetherPackingList;

import java.util.List;

@Repository
public interface TogetherPackingListRepository extends JpaRepository<TogetherPackingList, Long> {
    boolean existsByInviteCode(String inviteCode);

    @Modifying(clearAutomatically = true)
    @Query("update TogetherPackingList tp set tp.group = null where tp.group in :groups")
    void updateGroupNull(@Param("groups") List<Group> groups);
}
