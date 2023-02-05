package packman.validator;

import packman.util.CustomException;
import packman.util.ResponseCode;

public class LengthValidator {
    public static void validateListLength(String title) {
        if (title.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }
    }

    public static void validatePackLength(String pack) {
        if (pack.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LEN);
        }
    }

    public static void validateCategoryLength(String name) {
        if (name.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LEN);
        }
    }
}
