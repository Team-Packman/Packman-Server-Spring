package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.member.MemberResponseDto;
import packman.dto.member.MemberUserDto;
import packman.entity.User;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.UserRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.Validator.validateEmptyUserInMember;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final UserRepository userRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;

    public MemberResponseDto getMember(Long userId, Long listId) {
        validateUserId(userRepository, userId);
        TogetherPackingList togetherPackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId).getTogetherPackingList();
        PackingList packingList = togetherPackingList.getPackingList();

        long remainDay = ChronoUnit.DAYS.between(LocalDate.now(), packingList.getDepartureDate());
        List<UserGroup> userGroups = togetherPackingList.getGroup().getUserGroups();

        List<MemberUserDto> memberUserDtos = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            User member = userGroup.getUser();
            memberUserDtos.add(MemberUserDto.builder()
                    .id(member.getId().toString())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build());
        }

        // member에 아무도 없을 때
        validateEmptyUserInMember(userGroups.size());

        // 멤버에 존재하지 않는 유저일 때
        validateUserMemberId(userGroups, userId);

        return MemberResponseDto.builder()
                .title(packingList.getTitle())
                .departureDate(packingList.getDepartureDate().toString())
                .remainDay(String.valueOf(remainDay))
                .member(memberUserDtos)
                .inviteCode(togetherPackingList.getInviteCode())
                .build();
    }
}