package packman.validator;

import packman.entity.Category;
import packman.entity.Folder;
import packman.entity.Pack;
import packman.entity.UserGroup;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

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

    public static AlonePackingList validateAlonePackingListByInviteCode(AlonePackingListRepository alonePackingListRepository, String inviteCode) {
        return alonePackingListRepository.findByInviteCodeAndIsAloned(inviteCode, true).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static TogetherPackingList validateTogetherPackingListByInviteCode(TogetherPackingListRepository togetherPackingListRepository, String inviteCode) {
        return togetherPackingListRepository.findByInviteCode(inviteCode).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }
}
