package packman.dto.folder;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AloneFoldersResponseDto {
    private ArrayList<AloneFolderMapping> aloneFolder;

    public AloneFoldersResponseDto(ArrayList<AloneFolderMapping> aloneFolderMappings) {
        this.aloneFolder = aloneFolderMappings;
    }
}
