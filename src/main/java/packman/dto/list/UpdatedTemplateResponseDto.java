package packman.dto.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdatedTemplateResponseDto {
    String id;
    @JsonProperty("isSaved")
    boolean isSaved;
}
