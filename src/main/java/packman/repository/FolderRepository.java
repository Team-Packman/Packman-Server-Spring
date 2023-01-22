package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderMapping;
import packman.entity.Folder;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    ArrayList<FolderMapping> findByUserIdAndIsAlonedOrderByUserIdDesc(Long userId, boolean isAloned);

    Optional<Folder> findByIdAndUserId(Long id, Long userId);


}
