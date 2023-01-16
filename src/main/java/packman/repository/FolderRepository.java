package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
}
