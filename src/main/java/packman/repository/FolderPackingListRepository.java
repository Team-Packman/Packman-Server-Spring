package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.FolderPackingList;

import java.util.Optional;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
    Optional<FolderPackingList> findByFolder_UserIdAndAlonePackingListId(Long userId, Long listId);
}
