package packman.repository.basicTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.template.TemplateIdTitleMapping;
import packman.dto.template.TemplateResponseMapping;
import packman.entity.packingList.AlonePackingList;
import packman.entity.template.BasicTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasicTemplateRepository extends JpaRepository<BasicTemplate, Long> {
    Optional<BasicTemplate> findByIdAndIsDeleted(Long templateId, boolean isDeleted);

    List<TemplateIdTitleMapping> findByUserIdAndIsAlonedAndIsDeletedOrderByCreatedAt(Long userId, boolean isAloned, boolean isDeleted);

    TemplateResponseMapping findProjectionById(Long templateId);
}
