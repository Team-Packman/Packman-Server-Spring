package packman.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FolderResponseDto {
    private List<FolderInfo> aloneFolder;
    private List<FolderInfo> togetherFolder;
}
