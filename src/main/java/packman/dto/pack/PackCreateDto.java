package packman.dto.pack;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PackCreateDto {
    @NotNull
    String name;

    @NotNull
    String categoryId;

    @NotNull
    String listId;
}
