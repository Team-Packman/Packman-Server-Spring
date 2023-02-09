package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.entity.Pack;
import packman.entity.packingList.PackingList;

import java.util.List;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    List<Pack> findByCategory_PackingListAndPackerId(PackingList togetherListId, Long PackerId);

    @Modifying(clearAutomatically = true)
    @Query("update Pack p set p.packer = null where p in :packs")
    void updatePackerNull(@Param("packs") List<Pack> packs);
}
