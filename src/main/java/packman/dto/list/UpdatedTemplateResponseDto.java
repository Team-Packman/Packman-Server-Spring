package packman.dto.list;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdatedTemplateResponseDto {
    String id;
    boolean isSaved;
}
