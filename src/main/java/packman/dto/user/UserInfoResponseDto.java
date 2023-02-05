package packman.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponseDto {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;
}
