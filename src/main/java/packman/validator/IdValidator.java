package packman.validator;

import packman.entity.User;
import packman.entity.packingList.AlonePackingList;
import packman.entity.template.Template;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;

public class IdValidator {
    public static User validateUserId(UserRepository userRepository, Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        return user;
    }

    public static Template validateTemplateId(TemplateRepository templateRepository, Long templateId){
        Template template = templateRepository.findByIdAndIsDeleted(templateId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_TEMPLATE)
        );

        return template;
    }

    public static List<AlonePackingList> validateAloneListIds(AlonePackingListRepository alonePackingListRepository, List<Long> aloneListIds) {
        List<AlonePackingList> alonePackingLists = alonePackingListRepository.findByIdInAndIsAlonedAndPackingList_IsDeleted(aloneListIds, true, false);
        if(alonePackingLists.size() != aloneListIds.size()) { throw new CustomException(ResponseCode.NO_LIST);}

        return alonePackingLists;
    }
}
