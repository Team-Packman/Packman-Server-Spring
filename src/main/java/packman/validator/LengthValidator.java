package packman.validator;

import packman.util.CustomException;
import packman.util.ResponseCode;

public class LengthValidator {
    public static void validateListLength(String title) {
        if (title.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }
    }

    public static void validateUserNicknameLength(String nickname) {
        if (nickname.length() > 4) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }
    }
}
