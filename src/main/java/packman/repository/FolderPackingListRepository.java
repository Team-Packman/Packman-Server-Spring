package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Folder;
import packman.entity.FolderPackingList;

import java.util.ArrayList;

@Repository
public interface FolderPackingListRepository extends JpaRepository<FolderPackingList, Long> {
    ArrayList<FolderPackingList> findByFolder(Folder folder);
}
