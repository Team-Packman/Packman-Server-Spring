package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderAloneListMapping;
import packman.entity.FolderPackingList;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
    List<FolderAloneListMapping> findByFolderIdAndAlonePackingList_IsAlonedOrderByIdDesc(Long folderId, boolean isAloned);
    Optional<FolderPackingList> findByFolder_UserIdAndAlonePackingListId(Long userId, Long listId);
    Optional<FolderPackingList> findByFolder_UserIdAndAlonePackingListIdAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(Long userId, Long listId, boolean isAloned, boolean isDeleted);
}
