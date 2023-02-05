package packman.dto.folder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FolderUpdateRequestDto {
    private String id;
    private String name;
}
