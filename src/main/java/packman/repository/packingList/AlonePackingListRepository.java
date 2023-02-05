package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.AlonePackingList;

@Repository
public interface AlonePackingListRepository extends JpaRepository<AlonePackingList, Long> {
    boolean existsByInviteCode(String inviteCode);
}
