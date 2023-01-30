package packman.validator;

import packman.util.CustomException;
import packman.util.ResponseCode;

public class LengthValidator {
    public static void validatePackLength(String pack) {
        if (pack.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LEN);
        }
    }
}
