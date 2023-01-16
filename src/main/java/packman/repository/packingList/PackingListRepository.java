package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Folder;

@Repository
public interface PackingListRepository extends JpaRepository<Folder, Long> {
}
