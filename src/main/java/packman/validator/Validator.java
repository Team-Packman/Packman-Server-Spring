package packman.validator;

import packman.entity.packingList.AlonePackingList;
import packman.util.CustomException;
import packman.util.ResponseCode;

public class Validator {
    public static void validateUserAloneList(Long userId, AlonePackingList alonePackingList) {
        if (!alonePackingList.getFolderPackingList().getFolder().getUser().getId().equals(userId)) {
            throw new CustomException(ResponseCode.NO_LIST);
        }
    }
}
