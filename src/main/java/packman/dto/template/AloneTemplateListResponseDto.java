package packman.dto.template;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AloneTemplateListResponseDto {
    List<TemplateIdTitleMapping> basicTemplate;
    List<TemplateIdTitleMapping> myTemplate;
}
