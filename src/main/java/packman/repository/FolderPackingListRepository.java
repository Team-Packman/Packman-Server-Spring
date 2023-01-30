package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderAloneListMapping;
import packman.entity.FolderPackingList;

import java.util.List;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
    List<FolderAloneListMapping> findByFolderIdAndAlonePackingList_IsAlonedOrderByIdDesc(Long folderId, boolean isAloned);
}
