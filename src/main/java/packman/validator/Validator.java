package packman.validator;

import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.template.Template;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.PackRepository;
import packman.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import packman.entity.FolderPackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.entity.Category;
import packman.entity.Folder;
import packman.entity.Pack;
import packman.entity.UserGroup;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.PackRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;
import static packman.validator.IdValidator.*;

@RequiredArgsConstructor
public class Validator {
    public static FolderPackingList validateUserList(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        return folderPackingListRepository.findByFolder_UserIdAndAlonePackingListId(userId, listId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static Folder validateUserFolder(FolderRepository folderRepository, Long folderId, Long userId, boolean isAloned) {
        return folderRepository.findByIdAndUserIdAndIsAloned(folderId, userId, isAloned).orElseThrow(
                () -> new CustomException(ResponseCode.NO_FOLDER)
        );
    }

    public static PackingList validateUserAloneList(Long userId, Long aloneListId, AlonePackingListRepository alonePackingListRepository, PackingListRepository packingListRepository) {
        PackingList packingList = validatePackingListId(packingListRepository, aloneListId);
        AlonePackingList alonePackingList = validateAlonePackingListId(alonePackingListRepository, aloneListId);

        if (!alonePackingList.getFolderPackingList().getFolder().getUser().getId().equals(userId)) {
            throw new CustomException(ResponseCode.NO_LIST);
        }

        return packingList;
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

        validateUserGroupUserId(userGroups, userId);

        return packingList;
    }

    public static void validateEmptyUserInMember(int memberLength) {
        if (memberLength == 0) {
            throw new CustomException(ResponseCode.EMPTY_MEMBER);
        }
    }

    public static User validateUserGroupUser(UserGroup userGroup) {
        if (userGroup.getUser().isDeleted()) {
            throw new CustomException(ResponseCode.NO_USER);
        }
        return userGroup.getUser();
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

    public static TogetherAlonePackingList validateUserTogetherListIsSaved(TogetherAlonePackingListRepository togetherAlonePackingListRepository, Long linkId, User user, boolean isSaved) {
        TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByIdAndTogetherPackingList_PackingList_IsDeletedAndTogetherPackingList_Group_UserGroups_UserAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(linkId, false, user, false, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
        if (togetherAlonePackingList.getAlonePackingList().getPackingList().getIsSaved() != isSaved) {
            throw new CustomException(ResponseCode.NO_LIST);
        }

        return togetherAlonePackingList;
    }
    public static Pack validateListPack(PackRepository packRepository, PackingList packingList, Long packId){
        return packRepository.findByIdAndCategory_PackingList(packId,packingList).orElseThrow(
                () -> new CustomException(ResponseCode.NO_PACK)
        );
    }

    public static UserGroup validateUserInUserGroup(UserGroupRepository userGroupRepository, Group group, Long packerId) {
        return userGroupRepository.findByGroupAndUserIdAndUser_IsDeleted(group, packerId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_PACKER)
        );
    }
    public static void validateTogetherListDeleted(TogetherPackingList togetherPackingList) {
        PackingList packingList = togetherPackingList.getPackingList();
        if (packingList.getIsDeleted()) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }

    public static void validateUserTemplate(Template template, User user) {
        if (template.getUser() != null && template.getUser() != user) {
            throw new CustomException(ResponseCode.NO_TEMPLATE);
        }
    }

    public static List<FolderPackingList> validateFolderLists(FolderPackingListRepository folderPackingListRepository, Long folderId, List<Long> listIds) {
        List<FolderPackingList> folderPackingLists = folderPackingListRepository.findByFolderIdAndAlonePackingListIdIn(folderId, listIds);
        if(folderPackingLists.size() != listIds.size()) { throw new CustomException(ResponseCode.NO_FOLDER_LIST);}

        return folderPackingLists;
    }

    public static void validateNoDeleteMaker(List<Long> members, Long userId) {
        if (members.contains(userId)) {
            throw new CustomException(ResponseCode.NO_DELETE_MAKER);
        }
    }

    public static Pack validateListCategoryPack(PackingList packingList, Long categoryId, Long packId, CategoryRepository categoryRepository, PackRepository packRepository) {
        Category category = validateCategoryId(categoryRepository, categoryId);
        Pack pack = validatePackId(packRepository, packId);

        validateListCategory(packingList, category);
        validateCategoryPack(category, pack);

        return pack;
    }
}