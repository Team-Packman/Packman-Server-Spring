package packman.dto.pack;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class PackCreateDto {
    @NotEmpty
    String name;

    @NotBlank
    String categoryId;

    @NotBlank
    String listId;
}
