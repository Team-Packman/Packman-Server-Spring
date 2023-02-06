package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.list.TogetherAloneListMapping;
import packman.entity.User;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;

import java.util.Optional;

@Repository
public interface TogetherAlonePackingListRepository extends JpaRepository<TogetherAlonePackingList, Long> {
    TogetherAloneListMapping findByAlonePackingListId(Long myListId);
    TogetherAlonePackingList findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(TogetherPackingList together, Long userId);
    Optional<TogetherAlonePackingList> findByIdAndTogetherPackingList_PackingList_IsDeletedAndTogetherPackingList_Group_UserGroups_UserAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(Long linkId, boolean togetherIsDeleted, User user, boolean isAloned, boolean aloneIsDeleted );
}
