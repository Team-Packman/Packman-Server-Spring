package packman.validator;

import packman.entity.Category;
import packman.entity.Pack;
import packman.util.CustomException;
import packman.util.ResponseCode;

public class Validator {
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
