package packman.dto.folder;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AloneFoldersResponseDto {
    private ArrayList<FolderIdNameMapping> aloneFolder;

    public AloneFoldersResponseDto(ArrayList<FolderIdNameMapping> folderIdNameMappings) {
        this.aloneFolder = folderIdNameMappings;
    }
}
