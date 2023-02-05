package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.template.TemplateCategory;

import java.util.ArrayList;

@Repository
public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Long> {
}
