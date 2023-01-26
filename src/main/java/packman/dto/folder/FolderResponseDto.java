package packman.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class FolderResponseDto {
    private ArrayList<FolderMapping> aloneFolder;
    private ArrayList<FolderMapping> togetherFolder;
}
