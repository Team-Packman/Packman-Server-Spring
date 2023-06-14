package packman.validator;

import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.entity.template.BasicTemplate;
import packman.entity.template.Template;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.PackRepository;
import packman.repository.UserRepository;
import packman.repository.basicTemplate.BasicTemplateRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;
import java.util.stream.Collectors;

public class IdValidator {
    public static User validateUserId(UserRepository userRepository, Long userId) {
        return userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
    }

    public static void validateMemberId(UserRepository userRepository, List<Long> ids) {
        for (Long id : ids) {
            userRepository.findByIdAndIsDeleted(id, false).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_MEMBER)
            );
        }
    }
    public static Template validateTemplateId(TemplateRepository templateRepository, Long templateId) {
        return templateRepository.findByIdAndIsDeleted(templateId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_TEMPLATE)
        );
    }

    public static BasicTemplate validateBasicTemplateId(BasicTemplateRepository basicTemplateRepository, Long templateId) {
        return basicTemplateRepository.findByIdAndIsDeleted(templateId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_TEMPLATE)
        );
    }

    public static List<AlonePackingList> validateAloneListIds(AlonePackingListRepository alonePackingListRepository, List<Long> aloneListIds) {
        List<AlonePackingList> alonePackingLists = alonePackingListRepository.findByIdInAndIsAlonedAndPackingList_IsDeleted(aloneListIds, true, false);
        if(alonePackingLists.size() != aloneListIds.size()) { throw new CustomException(ResponseCode.NO_LIST);}

        return alonePackingLists;
    }

    public static List<TogetherAlonePackingList> validateTogetherListIds(TogetherAlonePackingListRepository togetherAlonePackingListRepository, List<Long> linkListIds) {
        List<TogetherAlonePackingList> togetherAlonePackingLists = togetherAlonePackingListRepository.findByIdInAndTogetherPackingList_PackingList_IsDeletedAndAlonePackingList_PackingList_IsDeletedAndAlonePackingList_IsAloned(linkListIds, false, false, false);
        if (togetherAlonePackingLists.size() != linkListIds.size()) {
            throw new CustomException(ResponseCode.NO_LIST);
        }

        return togetherAlonePackingLists;
    }

    public static Category validateCategoryId(CategoryRepository categoryRepository, Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_CATEGORY)
        );
    }

    public static List<Long> validateUserGroupUserId(List<UserGroup> userGroups, Long userId) {
        List<Long> userIdInGroup = userGroups.stream()
                .map(userGroup -> userGroup.getUser().getId())
                .collect(Collectors.toList());

        if (!userIdInGroup.contains(userId)) {
            throw new CustomException(ResponseCode.NO_MEMBER_USER);
        }

        return userIdInGroup;
    }

    public static void validateMemberUserId(List<UserGroup> userGroups, Long userId, List<Long> memberIds) {
        List<Long> userIdInGroup = validateUserGroupUserId(userGroups, userId);

        for (Long memberId : memberIds) {
            if (!userIdInGroup.contains(memberId)) {
                throw new CustomException(ResponseCode.NO_MEMBER_USER);
            }
        }
    }


    public static AlonePackingList validateAlonePackingListId(AlonePackingListRepository alonePackingListRepository, Long aloneId) {
        return alonePackingListRepository.findByIdAndPackingList_IsDeleted(aloneId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static PackingList validatePackingListId(PackingListRepository packingListRepository, Long listId) {
        return packingListRepository.findByIdAndIsDeleted(listId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static Pack validatePackId(PackRepository packRepository, Long packId) {
        return packRepository.findById(packId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_PACK)
        );
    }
    public static void validatePackingListIdInUser(PackingList packingList, Long userId) {
        if (packingList.getAlonePackingList() == null || packingList.getAlonePackingList().getFolderPackingList().getFolder().getUser().getId() != userId) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }

    public static TogetherPackingList validateTogetherPackingListId(TogetherPackingListRepository togetherPackingListRepository, Long togetherId) {
        return togetherPackingListRepository.findById(togetherId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static TogetherPackingList validateUserTogetherPackingListId(TogetherPackingListRepository togetherPackingListRepository, Long togetherListId, User user) {
        return togetherPackingListRepository.findByIdAndPackingList_IsDeletedAndGroup_UserGroups_User(togetherListId, false, user).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static FolderPackingList validateUserAloneListId(FolderPackingListRepository folderPackingListRepository, Long userId, Long listId) {
        return folderPackingListRepository.findByFolder_UserIdAndAlonePackingListIdAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(userId, listId, true, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static TogetherPackingList validateTogetherPackingInviteCode(TogetherPackingListRepository togetherPackingListRepository, String inviteCode) {
        TogetherPackingList togetherPackingList = togetherPackingListRepository
                .findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_LIST));
        if (togetherPackingList.getPackingList().getIsDeleted() == true) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
        return togetherPackingList;
    }

    public static TogetherAlonePackingList validateTogetherAlonePackingListIdInDetail(TogetherAlonePackingListRepository togetherAlonePackingListRepository, Long linkId) {
        return togetherAlonePackingListRepository.findByIdAndTogetherPackingList_PackingList_IsDeletedAndAlonePackingList_IsAlonedAndAlonePackingList_PackingList_IsDeleted(linkId, false, false, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static TogetherAlonePackingList validateTogetherAlonePackingListId(TogetherAlonePackingListRepository togetherAlonePackingListRepository, Long integratedId) {
        return togetherAlonePackingListRepository.findById(integratedId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }

    public static void validateNoMakerId(Long makerId, Long userId) {
        if (!makerId.equals(userId)) {
            throw new CustomException(ResponseCode.NO_MAKER);
        }
    }
}
