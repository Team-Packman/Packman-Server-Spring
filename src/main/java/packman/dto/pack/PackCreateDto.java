package packman.dto.pack;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class PackCreateDto {
    @NotEmpty
    private String name;

    @NotBlank
    private String categoryId;

    @NotBlank
    private String listId;
}
