package packman.repository.packingList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Folder;
import packman.entity.packingList.AlonePackingList;

import java.util.ArrayList;

@Repository
public interface AlonePackingListRepository extends JpaRepository<AlonePackingList, Long> {
    ArrayList<AlonePackingList> findByFolderPackingListFolder(Folder folder);
}
