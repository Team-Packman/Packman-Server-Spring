package packman.dto.list;

import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryResponseDto;

@Builder
@Data
public class TogetherListResponseDto {
        String id; //함께-나의 연동 id
        String title;
        String departureDate;
        TogetherListDto togetherPackingList;
        CategoryResponseDto myPackingList;
}
