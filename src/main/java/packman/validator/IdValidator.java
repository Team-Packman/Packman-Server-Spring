package packman.validator;

import packman.entity.Category;
import packman.entity.packingList.AlonePackingList;
import packman.repository.CategoryRepository;
import packman.repository.PackRepository;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

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

    public static Pack validatePackId(PackRepository packRepository, Long packId) {
        return packRepository.findById(packId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_PACK)
        );
    }
}
