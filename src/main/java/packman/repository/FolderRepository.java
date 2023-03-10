package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderIdNameMapping;
import packman.entity.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    ArrayList<Folder> findByUserIdOrderByIdDesc(Long userId);

    Optional<Folder> findByIdAndUserId(Long folderId, Long userId);

    Optional<FolderIdNameMapping> findByIdAndIsAloned(Long folderId, boolean isAloned);

    Optional<Folder> findByIdAndUserIdAndIsAloned(Long folderId, Long userId, boolean isAloned);

    List<FolderIdNameMapping> findByUserIdAndIsAlonedOrderByIdDesc(Long userId, boolean isAloned);

    Optional<Folder> findByUserIdAndNameAndIsAloned(Long userId, String name, boolean isAloned);

    @Modifying(clearAutomatically = true)
    @Query("delete from Folder f where f.id = :folderId")
    void deleteFolderByFolderId(@Param("folderId") Long folderId);
}

