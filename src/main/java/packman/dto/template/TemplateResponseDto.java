package packman.dto.template;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class TemplateResponseDto {
    String id;
    String title;
    List<TemplateCategoryMapping> category;
}
