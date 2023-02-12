package packman.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberUserDto {
    private String id;
    private String nickname;
    private String profileImage;
}
