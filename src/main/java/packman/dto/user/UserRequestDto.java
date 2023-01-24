package packman.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor

public class UserRequestDto {
    private String email;
    private String name;
    private String gender;
    private String ageRange;
    private String nickname;
    private String profileImage;
    private String path;
}
