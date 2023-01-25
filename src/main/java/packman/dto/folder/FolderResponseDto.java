package packman.dto.folder;

import lombok.Data;

import java.util.ArrayList;

@Data
public class FolderResponseDto {
    private ArrayList<FolderMapping> aloneFolder;
    private ArrayList<FolderMapping> togetherFolder;


    public FolderResponseDto(ArrayList<FolderMapping> aloneFolder, ArrayList<FolderMapping> togetherFolder) {
        this.aloneFolder = aloneFolder;
        this.togetherFolder = togetherFolder;
    }
}
