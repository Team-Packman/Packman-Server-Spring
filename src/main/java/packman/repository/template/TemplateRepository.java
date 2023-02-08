package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.dto.template.TemplateResponseMapping;
import packman.entity.template.Template;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByIdAndIsDeleted(Long templateId, boolean isDeleted);
    TemplateResponseMapping findProjectionById(Long templateId);
}
