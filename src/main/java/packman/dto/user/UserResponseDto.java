package packman.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"isAlreadyUser", "id", "email", "name", "gender", "ageRange", "nickname", "profileImage", "accessToken", "refreshToken", "path" })
public class UserResponseDto{
    @JsonProperty("isAlreadyUser")
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

