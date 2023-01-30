package packman.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import packman.dto.list.ListInFolderDto;

import java.util.List;

@Data
@AllArgsConstructor
public class TogetherListsInFolderResponseDto {
    private FolderIdNameMapping currentFolder;

    private List<FolderIdNameMapping> folder;

    private String listNum;

    private List<ListInFolderDto> TogetherPackingList;
}
