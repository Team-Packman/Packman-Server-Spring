package packman.dto.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
public interface TemplateResponseMapping {
    String getId();

    String getTitle();
    List<TemplateCategoryMapping> getCategories();
}
