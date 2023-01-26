package packman.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor

public class UserRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String name;
    private String gender;
    private String ageRange;
    @NotBlank
    private String nickname;
    @NotBlank
    private String profileImage;
    @NotBlank
    private String path;
}
