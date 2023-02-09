package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.dto.list.ListResponseMapping;
import packman.entity.packingList.PackingList;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackingListRepository extends JpaRepository<PackingList, Long> {
    Optional<PackingList> findByIdAndIsDeleted(Long listId, boolean isDeleted);
    ListResponseMapping findByIdAndTitle(Long listId, String title);
    @Modifying(clearAutomatically = true)
    @Query("update PackingList p set p.isDeleted = true where p in :packingLists")
    void updatelistIsDeletedTrue(@Param("packingLists") List<PackingList> packingLists);
}
