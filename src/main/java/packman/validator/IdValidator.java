package packman.validator;

import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
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
                .map( userGroup -> userGroup.getUser().getId())
                .collect(Collectors.toList());
        if (!userIdInGroup.contains(userId)) {
            throw new CustomException(ResponseCode.NO_MEMBER_USER);
        }
    }
}
