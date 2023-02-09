package packman.dto.folder;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FolderUpdateRequestDto {
    @NotNull
    private String id;
    @NotNull
    private String name;
}
