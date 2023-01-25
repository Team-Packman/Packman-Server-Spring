package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.list.ListIdDtoMapping;
import packman.entity.FolderPackingList;

import java.util.List;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
    List<ListIdDtoMapping> findByFolderIdAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeletedOrderByIdDesc(Long folderId, boolean isAloned, boolean isDeleted);
}
