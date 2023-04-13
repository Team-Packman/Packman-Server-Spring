package packman.dto.list;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartureDateResponseDto {
    private String id;
    private String departureDate;
}
