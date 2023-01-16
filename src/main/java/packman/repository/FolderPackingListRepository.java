package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.FolderPackingList;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
}
