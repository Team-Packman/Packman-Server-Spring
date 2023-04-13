package packman.dto.list;

import lombok.Builder;
import lombok.Data;
import packman.dto.list.alone.MyListDto;

@Builder
@Data
public class TogetherListResponseDto {
        String id; //함께-나의 연동 id
        String title;
        String departureDate;
        TogetherListDto togetherPackingList;
        MyListDto myPackingList;
}
