package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;

import java.util.Optional;

@Repository
public interface AlonePackingListRepository extends JpaRepository<AlonePackingList, Long> {
    boolean existsByInviteCode(String inviteCode);
    Optional<AlonePackingList> findByIdAndIsAlonedAndPackingList_IsDeleted(Long listId, boolean isAloned, boolean isDeleted);
}
