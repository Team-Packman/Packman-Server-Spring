package packman.validator;

import packman.entity.*;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.template.Template;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.validator.IdValidator.*;

import java.util.List;

import static packman.validator.IdValidator.*;

public class Validator {
    public static void validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static Folder validateUserFolder(FolderRepository folderRepository, Long folderId, Long userId, boolean isAloned) {
        return folderRepository.findByIdAndUserIdAndIsAloned(folderId, userId, isAloned).orElseThrow(
                () -> new CustomException(ResponseCode.NO_FOLDER)
        );
    }

    public static void validateUserAloneList(Long userId, AlonePackingList alonePackingList) {
        if (!alonePackingList.getFolderPackingList().getFolder().getUser().getId().equals(userId)) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }

    public static void validateListCategory(PackingList packingList, Category category) {
        if (!category.getPackingList().equals(packingList)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
    }

    public static void validateCategoryPack(Category category, Pack pack) {
        if (!pack.getCategory().equals(category)) {
            throw new CustomException(ResponseCode.NO_CATEGORY_PACK);
        }
    }

    public static PackingList validateTogetherList(Long userId,
                                                   Long togetherListId,
                                                   PackingListRepository packingListRepository,
                                                   TogetherPackingListRepository togetherPackingListRepository) {
        PackingList packingList = validatePackingListId(packingListRepository, togetherListId);
        validateTogetherPackingListId(togetherPackingListRepository, togetherListId);

        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        return packingList;
    }

    public static Template validateListTemplate(TemplateRepository templateRepository, AlonePackingList aloneList){
        return templateRepository.findByAlonePackingListAndIsDeleted(aloneList, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_TEMPLATE)
        );
    }

    public static AlonePackingList validateUserAloneListIsSaved(FolderPackingListRepository folderPackingListRepository,Long userId, Long listId, boolean isSaved){
        FolderPackingList folderPackingList = validateUserAloneListId(folderPackingListRepository, userId, listId);
        if(folderPackingList.getAlonePackingList().getPackingList().getIsSaved() != isSaved){
            throw new CustomException(ResponseCode.NO_LIST);
        }
        return folderPackingList.getAlonePackingList();
    }

    public static TogetherAlonePackingList validateUserTogetherListIsSaved(TogetherAlonePackingListRepository togetherAlonePackingListRepository, Long linkId, User user, boolean isSaved){
        TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByIdAndTogetherPackingList_PackingList_IsDeletedAndTogetherPackingList_Group_UserGroups_UserAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(linkId, false, user, false, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
        if(togetherAlonePackingList.getAlonePackingList().getPackingList().getIsSaved() != isSaved){
            throw new CustomException(ResponseCode.NO_LIST);
        }

        return togetherAlonePackingList;
    }
}
