package packman.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CategoryUpdateDto {
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String listId;
}
