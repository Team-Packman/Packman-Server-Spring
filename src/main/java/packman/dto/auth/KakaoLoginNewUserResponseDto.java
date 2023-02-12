package packman.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"alreadyUser", "email", "name", "gender", "ageRange"})
public class KakaoLoginNewUserResponseDto {
    @JsonProperty("isAlreadyUser")
    private boolean alreadyUser;
    private String email;
    private String name;
    private String gender;
    private String ageRange;
}