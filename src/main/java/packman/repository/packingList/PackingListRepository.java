package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.category.CategoryResponseDto;
import packman.entity.packingList.PackingList;

import java.util.Optional;

@Repository
public interface PackingListRepository extends JpaRepository<PackingList, Long> {
    Optional<PackingList> findByIdAndIsDeleted(Long listId, boolean isDeleted);

    CategoryResponseDto findByIdAndTitle(Long listId, String title);
}
