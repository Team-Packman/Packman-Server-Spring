package packman.validator;

import packman.util.CustomException;
import packman.util.ResponseCode;

public class LengthValidator {
    public static void validateCategoryLength(String name) {
        if (name.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LEN);
        }
    }
}
