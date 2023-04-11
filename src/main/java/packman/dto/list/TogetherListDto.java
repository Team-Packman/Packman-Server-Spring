package packman.dto.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryInfo;

import java.util.List;

@Builder
@Data
public class TogetherListDto {
    String id;
    String groupId;
    List<CategoryInfo> category;
    String inviteCode;
}
