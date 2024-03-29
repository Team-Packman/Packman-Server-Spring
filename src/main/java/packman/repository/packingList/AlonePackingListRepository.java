package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.TogetherPackingList;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlonePackingListRepository extends JpaRepository<AlonePackingList, Long> {
    boolean existsByInviteCode(String inviteCode);

    List<AlonePackingList> findByIdInAndIsAlonedAndPackingList_IsDeleted(List<Long> listIds, boolean isAloned, boolean isDeleted);

    Optional<AlonePackingList> findByIdAndPackingList_IsDeleted(Long id, boolean isDeleted);

    List<AlonePackingList> findByFolderPackingList_Folder_UserIdOrderByIdDesc(Long userId);
    
    Optional<AlonePackingList> findByInviteCodeAndIsAlonedAndPackingList_IsDeleted(String inviteCode, boolean isAloned, boolean isDeleted);

    List<AlonePackingList> findByTogetherAlonePackingList_TogetherPackingList(TogetherPackingList togetherPackingList);
}
