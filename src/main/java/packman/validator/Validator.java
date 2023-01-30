package packman.validator;


import lombok.RequiredArgsConstructor;
import packman.repository.FolderPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

@RequiredArgsConstructor
public class Validator {
    public static void validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }
}
