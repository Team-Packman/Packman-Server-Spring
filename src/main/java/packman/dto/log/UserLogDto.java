package packman.dto.log;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLogDto {
    private String email;
    private String name;
    private String gender;
    private String ageRange;
    private String nickname;
    private String profileImage;
    private String path;
}
