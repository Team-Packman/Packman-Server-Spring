package packman.dto.list.alone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryInfo;

import java.util.List;

@Builder
@Data
public class MyListDto {
    String id;

    List<CategoryInfo> category;

    @JsonProperty("isSaved")
    boolean isSaved;
}
