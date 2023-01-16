package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.template.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
