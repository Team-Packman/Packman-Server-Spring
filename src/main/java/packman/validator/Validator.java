package packman.validator;


import packman.entity.Category;
import packman.entity.FolderPackingList;
import packman.entity.packingList.AlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

public class Validator {
    public static AlonePackingList validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        FolderPackingList folderPackingList = folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );

        return folderPackingList.getAlonePackingList();
    }

    public static void validateListCategory(Long listId, Category category) {
        if (!category.getPackingList().getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
    }
}
