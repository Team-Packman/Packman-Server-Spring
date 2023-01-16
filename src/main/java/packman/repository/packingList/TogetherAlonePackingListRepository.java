package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.TogetherAlonePackingList;

@Repository
public interface TogetherAlonePackingListRepository extends JpaRepository<TogetherAlonePackingList, Long> {
}
