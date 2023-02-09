package packman.dto.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id", "name", "pack"})
public interface TemplateCategoryMapping {
    String getId();

    String getName();
    @JsonProperty("pack")
    List<TemplatePackInfo> getTemplatePacks();
}