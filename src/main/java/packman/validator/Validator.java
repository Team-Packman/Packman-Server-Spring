package packman.validator;


import packman.entity.Category;
import packman.entity.FolderPackingList;
import packman.entity.packingList.AlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.entity.Pack;
import packman.util.CustomException;
import packman.util.ResponseCode;

public class Validator {
    public static void validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        FolderPackingList folderPackingList = folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static void validateUserAloneList(Long userId, AlonePackingList alonePackingList) {
        if (!alonePackingList.getFolderPackingList().getFolder().getUser().getId().equals(userId)) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }

    public static void validateListCategory(Long listId, Category category) {
        if (!category.getPackingList().getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
    }

    public static void validateCategoryPack(Long categoryId, Pack pack) {
        if (!pack.getCategory().getId().equals(categoryId)) {
            throw new CustomException(ResponseCode.NO_CATEGORY_PACK);
        }
    }
}
