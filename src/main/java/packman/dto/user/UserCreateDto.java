package packman.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

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
