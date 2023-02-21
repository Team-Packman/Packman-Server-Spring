package packman.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
