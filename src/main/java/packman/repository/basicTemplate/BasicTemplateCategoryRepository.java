package packman.repository.basicTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import packman.entity.template.BasicTemplateCategory;

public interface BasicTemplateCategoryRepository extends JpaRepository<BasicTemplateCategory, Long> {
}
