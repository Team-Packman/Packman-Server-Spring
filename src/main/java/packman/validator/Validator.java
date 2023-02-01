package packman.validator;


import lombok.RequiredArgsConstructor;
import packman.repository.FolderPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.entity.Category;

@RequiredArgsConstructor
public class Validator {
    public static void validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static void validateListCategory(Long listId, Category category) {
        if (!category.getPackingList().getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
    }
}
