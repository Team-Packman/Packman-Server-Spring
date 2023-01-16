package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.TogetherPackingList;

@Repository
public interface TogetherPackingListRepository extends JpaRepository<TogetherPackingList, Long> {
}
