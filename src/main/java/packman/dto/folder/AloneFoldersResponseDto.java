package packman.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AloneFoldersResponseDto {
    private ArrayList<AloneFolderMapping> aloneFolder;
}
