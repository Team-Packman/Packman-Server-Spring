package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.template.TemplateIdTitleMapping;
import packman.entity.template.Template;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByIdAndIsDeleted(Long templateId, boolean isDeleted);
    List<TemplateIdTitleMapping> findByUserIdAndIsAlonedAndIsDeleted(Long userId, boolean isAloned, boolean isDeleted);
    List<TemplateIdTitleMapping> findByIsAlonedAndIsDeletedAndUserIdNull(boolean isAloned, boolean isDeleted);
}
