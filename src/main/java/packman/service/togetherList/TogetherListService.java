package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.togetherList.TogetherListInviteResponseDto;
import packman.entity.UserGroup;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.UserGroupRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListService {
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final UserGroupRepository userGroupRepository;

    public TogetherListInviteResponseDto getInviteTogetherList(Long userId, String inviteCode) {
        // invitecode로 존재하는 패킹리스트인지 확인, 삭제되지 않은 패킹리스트인지 확인
        TogetherPackingList togetherPackingList = togetherPackingListRepository
                .findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_LIST));
        if (togetherPackingList.getPackingList().getIsDeleted() == true) {
            throw new CustomException(ResponseCode.NO_LIST);
        }

        // 이미 추가된 멤버인지 확인
        Optional<UserGroup> userGroup = userGroupRepository.findByGroupAndUserId(togetherPackingList.getGroup(), userId);
        if (userGroup.isPresent()) {
//            ArrayList<AlonePackingList> Alone
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(togetherPackingList, userId);
            return new TogetherListInviteResponseDto(String.valueOf(togetherAlonePackingList.getId()), true);
        } else {
            return new TogetherListInviteResponseDto(String.valueOf(togetherPackingList.getId()), false);
        }
    }
}
