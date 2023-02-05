package packman.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CategoryCreateDto {
    @NotNull
    private String name;
    @NotNull
    private String listId;

}
