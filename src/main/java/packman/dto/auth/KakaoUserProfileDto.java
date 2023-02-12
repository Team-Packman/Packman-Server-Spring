package packman.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserProfileDto {
    private String name;
    private String email;
    private String ageRange;
    private String gender;
}
