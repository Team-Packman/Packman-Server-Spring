package packman.dto.list.alone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import packman.dto.list.ListResponseMapping;

@Builder
@Data
public class MyListDto {
    ListResponseMapping category;

    @JsonProperty("isSaved")
    boolean isSaved;
}
