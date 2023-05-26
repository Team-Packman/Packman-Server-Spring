package packman.dto.list;

import lombok.Builder;
import lombok.Data;
import packman.dto.list.alone.MyListDto;

@Builder
@Data
public class DetaildTogetherListResponseDto {
    String id;
    String folderId;
    TogetherListDto togetherPackingList;
    MyListDto myPackingList;
}
