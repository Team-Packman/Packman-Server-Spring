package packman.dto.log;

import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryInfo;

import java.util.List;

@Builder
@Data
public class TogetherListLogDto {
    private String id;
    private String templateId;
    private String title;
    private String departureDate;
    private String groupId;
    List<CategoryInfo> category;
}
