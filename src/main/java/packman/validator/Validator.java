package packman.validator;


import lombok.RequiredArgsConstructor;
import packman.entity.Folder;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

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
}
