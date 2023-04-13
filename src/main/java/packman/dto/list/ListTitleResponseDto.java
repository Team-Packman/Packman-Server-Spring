package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ListTitleResponseDto {
    private String id;
    private String title;
}
