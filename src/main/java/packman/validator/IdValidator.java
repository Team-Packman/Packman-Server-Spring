package packman.validator;

import packman.entity.Category;
import packman.entity.UserGroup;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;
import java.util.stream.Collectors;

public class IdValidator {
    public static PackingList validatePackingListId(PackingListRepository packingListRepository, Long packingListId) {
        return packingListRepository.findByIdAndIsDeleted(packingListId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
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

    public static Category validateCategoryId(CategoryRepository categoryRepository, Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_CATEGORY)
        );
    }
    public static AlonePackingList validateAlonePackingListId(AlonePackingListRepository alonePackingListRepository, Long aloneId) {
        return alonePackingListRepository.findById(aloneId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
    }
    public static void validatePackingListIdInUser(PackingList packingList, Long userId) {
        if (packingList.getAlonePackingList() == null || packingList.getAlonePackingList().getFolderPackingList().getFolder().getUser().getId() != userId) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }
}
