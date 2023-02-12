package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InviteAloneListResponseDto {
    private String id;
    private boolean IsOwner;
}
