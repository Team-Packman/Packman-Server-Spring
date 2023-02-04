package packman.validator;


import lombok.RequiredArgsConstructor;
import packman.entity.Folder;
import packman.entity.FolderPackingList;
import packman.entity.packingList.AlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;

@RequiredArgsConstructor
public class Validator {
    public static void validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static Folder validateUserFolder(FolderRepository folderRepository, Long folderId, Long userId, boolean isAloned) {
        Folder folder =  folderRepository.findByIdAndUserIdAndIsAloned(folderId, userId, isAloned).orElseThrow(
                () -> new CustomException(ResponseCode.NO_FOLDER)
        );

        return folder;
    }

    public static List<FolderPackingList> validateFolderLists(FolderPackingListRepository folderPackingListRepository, Long folderId, List<Long> listIds, int expectedListSize ) {
        List<FolderPackingList> folderPackingLists = folderPackingListRepository.findByFolderIdAndAlonePackingListIdIn(folderId, listIds);
        if(folderPackingLists.size() != expectedListSize) { throw new CustomException(ResponseCode.NO_FOLDER_LIST);}

        return folderPackingLists;
    }
}
