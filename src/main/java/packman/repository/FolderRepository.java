package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.folder.FolderMapping;
import packman.entity.Folder;

import java.util.ArrayList;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    ArrayList<FolderMapping> findByUserIdAndIsAlonedOrderByUserIdDesc(Long userId, boolean isAloned);

}
