package packman.validator;

import packman.entity.Category;
import packman.entity.UserGroup;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.Pack;
import packman.repository.CategoryRepository;
import packman.repository.PackRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;
import java.util.stream.Collectors;

public class IdValidator {
    public static void validateUserId(UserRepository userRepository, Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
    }

    public static Category validateCategoryId(CategoryRepository categoryRepository, Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_CATEGORY)
        );
    }

    public static void validateUserMemberId(List<UserGroup> userGroups, Long userId) {
        List<Long> userIdInGroup = userGroups.stream()
                .map(userGroup -> userGroup.getUser().getId())
                .collect(Collectors.toList());

        if (!userIdInGroup.contains(userId)) {
            throw new CustomException(ResponseCode.NO_MEMBER_USER);
        }
    }

    public static void validateTogetherPackingListId(TogetherPackingListRepository togetherPackingListRepository, Long togetherId) {
        togetherPackingListRepository.findById(togetherId).orElseThrow(
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
}
