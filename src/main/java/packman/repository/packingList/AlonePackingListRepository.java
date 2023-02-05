package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.AlonePackingList;

import java.util.List;

@Repository
public interface AlonePackingListRepository extends JpaRepository<AlonePackingList, Long> {
    List<AlonePackingList> findByFolderPackingList_Folder_UserIdOrderByIdDesc(Long userId);
}
