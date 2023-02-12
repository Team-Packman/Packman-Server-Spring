package packman.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AuthKakaoRequestDto {
    @NotBlank
    private String accessToken;
}
