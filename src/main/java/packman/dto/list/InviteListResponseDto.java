package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import packman.dto.category.CategoryInfo;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class InviteListResponseDto {
    private String id;
    private String title;
    private String departureDate;
    private List<CategoryInfo> category;
}
