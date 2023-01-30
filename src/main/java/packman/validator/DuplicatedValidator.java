package packman.validator;

import packman.entity.Category;
import packman.entity.packingList.PackingList;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;
import java.util.Optional;

public class DuplicatedValidator {
    public static void validateDuplicatedCategory(PackingList packingList, String categoryName, Long categoryId) {
        List<Category> categorys = packingList.getCategory();
        categorys.stream().forEach(category -> {
            if (category.getName().equals(categoryName)) {
                if(categoryId != null && category.getId() != categoryId) {
                    throw new CustomException(ResponseCode.DUPLICATED_CATEGORY);
                }
            }
        });
    }
}
