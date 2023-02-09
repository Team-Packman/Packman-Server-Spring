package packman.dto.template;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name"})
public interface TemplatePackInfo {
    String getId();

    String getName();

}
