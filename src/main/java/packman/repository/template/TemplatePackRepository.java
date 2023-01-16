package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packman.entity.TemplatePack;

@Repository
public interface TemplatePackRepository extends JpaRepository<TemplatePack, Long> {
}
