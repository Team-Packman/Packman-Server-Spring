package packman.dto.folder;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TogetherFoldersResponseDto {
    private ArrayList<FolderIdNameMapping> togetherFolder;

    public TogetherFoldersResponseDto(ArrayList<FolderIdNameMapping> folderIdNameMappings) {
        this.togetherFolder = folderIdNameMappings;
    }
}
