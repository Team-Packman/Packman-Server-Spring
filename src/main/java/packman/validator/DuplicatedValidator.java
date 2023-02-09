package packman.validator;

import packman.entity.Category;
import packman.entity.Group;
import packman.entity.User;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;

public class DuplicatedValidator {
    public static void validateDuplicatedCategory(PackingList packingList, String categoryName, Long categoryId) {
        List<Category> categorys = packingList.getCategory();
        categorys.stream().forEach(category -> {
            if (category.getName().equals(categoryName) && categoryId != category.getId()) {
                throw new CustomException(ResponseCode.DUPLICATED_CATEGORY);
            }
        });
    }
    public static void validateDuplicatedMember(Group group, User user) {
        List<UserGroup> members = group.getUserGroups();
        members.forEach(member -> {
                    if (member.getUser() == user) {
                        throw new CustomException(ResponseCode.DUPLICATED_MEMBER);
                    }
                }
        );
    }
}
