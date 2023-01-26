package packman.dto.folder;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FolderResponseDto {
    private List<FolderInfo> aloneFolder;
    private List<FolderInfo> togetherFolder;


    public FolderResponseDto(List<FolderInfo> aloneFolder, List<FolderInfo> togetherFolder) {
        this.aloneFolder = aloneFolder;
        this.togetherFolder = togetherFolder;
    }
}
