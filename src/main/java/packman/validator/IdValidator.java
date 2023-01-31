package packman.validator;

import packman.entity.User;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

public class IdValidator {
    public static User validateUserId(UserRepository userRepository, Long userId) {
        return userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
    }
}
