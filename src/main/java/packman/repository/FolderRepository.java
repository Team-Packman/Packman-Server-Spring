package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderIdNameMapping;
import packman.dto.folder.FolderMapping;
import packman.entity.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByIdAndUserId(Long folderId, Long userId);

    Optional<FolderIdNameMapping> findByIdAndIsAloned(Long folderId, boolean isAloned);

    List<FolderIdNameMapping> findByUserIdAndIsAlonedOrderByIdDesc(Long userId, boolean isAloned);
    ArrayList<FolderMapping> findByUserIdAndIsAlonedOrderByIdDesc(Long userId, boolean isAloned);

}