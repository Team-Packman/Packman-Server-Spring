package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecentCreatedListResponseDto {
    private String id;
    private String title;
    private String remainDay;
    private String packTotalNum;
    private String packRemainNum;
    private String url;
}
