package packman.dto.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryInfo;

@Builder
@Data
public class DetaildTogetherListResponseDto {
    String id;
    String folderId;
    TogetherListDto togetherPackingList;
    ListResponseMapping myPackingList;
    @JsonProperty("isMember")
    boolean isMember;
}
