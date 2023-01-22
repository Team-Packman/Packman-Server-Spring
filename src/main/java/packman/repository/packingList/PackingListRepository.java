package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.PackingList;

@Repository
public interface PackingListRepository extends JpaRepository<PackingList, Long> {
}
