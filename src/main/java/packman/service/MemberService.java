package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.member.MemberResponseDto;
import packman.dto.member.MemberUserDto;
import packman.entity.Group;
import packman.entity.User;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.GroupRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.service.togetherList.TogetherListService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.Validator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final UserRepository userRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final GroupRepository groupRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final TogetherListService togetherListService;

    public MemberResponseDto getMember(Long userId, Long listId) {
        validateUserId(userRepository, userId);
        TogetherPackingList togetherPackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId).getTogetherPackingList();
        PackingList packingList = togetherPackingList.getPackingList();

        long remainDay = ChronoUnit.DAYS.between(LocalDate.now(), packingList.getDepartureDate());
        List<UserGroup> userGroups = togetherPackingList.getGroup().getUserGroups();

        List<MemberUserDto> memberUserDtos = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            User member = validateUserGroupUser(userGroup);
            memberUserDtos.add(MemberUserDto.builder()
                    .id(member.getId().toString())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build());
        }

        // member에 아무도 없을 때
        validateEmptyUserInMember(userGroups.size());

        // 멤버에 존재하지 않는 유저일 때
        validateUserGroupUserId(userGroups, userId);

        return MemberResponseDto.builder()
                .title(packingList.getTitle())
                .departureDate(packingList.getDepartureDate().toString())
                .remainDay(String.valueOf(remainDay))
                .member(memberUserDtos)
                .inviteCode(togetherPackingList.getInviteCode())
                .build();
    }

    public void deleteMember(Long userId, Long listId, List<Long> memberIds) {
        validateUserId(userRepository, userId);
        Group group = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId)
                .getTogetherPackingList()
                .getGroup();

        validateMemberId(userRepository, memberIds);

        List<UserGroup> userGroups = group.getUserGroups();

        // 멤버에 아무도 없을 때
        validateEmptyUserInMember(userGroups.size());

        // 멤버에 존재하는 유저일 때
        validateMemberUserId(userGroups, userId, memberIds);

        // 삭제할 권한이 없는 유저일 때
        validateNoMakerId(userGroups.get(0).getUser().getId(), userId);

        // 삭제 권한이 있는 유저가 본인을 삭제하려고 할 때
        validateNoDeleteMaker(memberIds, userId);

        // 삭제된 멤버의 함께 패킹리스트 삭제
        for (Long id : memberIds) {
            List<Long> listIds = new ArrayList<>();

            TogetherPackingList togetherPackingList = togetherPackingListRepository.findByGroup(group);
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(togetherPackingList, id);
            listIds.add(togetherAlonePackingList.getId());
            togetherListService.deleteTogetherList(
                    id,
                    togetherAlonePackingList.getAlonePackingList().getFolderPackingList().getFolder().getId(),
                    listIds
            );
        }
    }
}