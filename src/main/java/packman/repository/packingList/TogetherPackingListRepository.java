package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Group;
import packman.entity.packingList.TogetherPackingList;

import java.util.Optional;

@Repository
public interface TogetherPackingListRepository extends JpaRepository<TogetherPackingList, Long> {
    boolean existsByInviteCode(String inviteCode);

    Optional<TogetherPackingList> findByInviteCode(String inviteCode);

    TogetherPackingList findByGroup(Group group);
}
