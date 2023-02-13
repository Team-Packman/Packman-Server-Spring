package packman.repository.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packman.entity.template.TemplateCategory;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Long> {
}
