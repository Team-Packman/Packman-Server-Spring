package packman.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"alreadyUser", "email", "name", "gender", "ageRange", "nickname", "profileImage", "accessToken", "refreshToken"})
public class KakaoLoginResponseDto {
    @JsonProperty("isAlreadyUser")
    private boolean alreadyUser;
    private String email;
    private String name;
    private String gender;
    private String ageRange;
    private String nickname;
    private String profileImage;
    private String accessToken;
    private String refreshToken;
}
