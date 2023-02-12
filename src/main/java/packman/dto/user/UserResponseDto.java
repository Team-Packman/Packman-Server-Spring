package packman.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDto{
    private boolean isAlreadyUser;
    private String id;
    private String email;
    private String name;
    private String gender;
    private String ageRange;
    private String nickname;
    private String profileImage;
    private String accessToken;
    private String refreshToken;
    private String path;
}

