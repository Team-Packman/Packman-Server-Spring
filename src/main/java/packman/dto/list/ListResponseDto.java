package packman.dto.list;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListResponseDto {
    private String id;
    private String title;
    private String departureDate;
}
