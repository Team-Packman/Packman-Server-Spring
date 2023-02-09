package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.list.TogetherAloneListMapping;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;

import java.util.List;

@Repository
public interface TogetherAlonePackingListRepository extends JpaRepository<TogetherAlonePackingList, Long> {
    TogetherAloneListMapping findByAlonePackingListId(Long myListId);
    List<TogetherAlonePackingList> findByIdInAndTogetherPackingList_PackingList_IsDeletedAndAlonePackingList_PackingList_IsDeletedAndAlonePackingList_IsAloned(List<Long> linkIds, boolean togetherIsDeleted, boolean aloneIsDeleted, boolean isAloned);
    TogetherAlonePackingList findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(TogetherPackingList together, Long userId);
}
