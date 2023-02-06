package packman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.Pack;
import packman.entity.packingList.PackingList;

import java.util.Optional;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    Optional<Pack> findByIdAndCategory_PackingList(Long packId, PackingList packingList);
}
