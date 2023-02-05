package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.member.MemberAddDto;
import packman.dto.member.MemberResponseDto;
import packman.dto.togetherList.TogetherListInviteResponseDto;
import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.*;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;

import java.util.Optional;

import static packman.validator.DuplicatedValidator.validateDuplicatedMember;
import static packman.validator.IdValidator.*;
import static packman.validator.Validator.validateTogetherListDeleted;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListService {
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final PackingListRepository packingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final CategoryRepository categoryRepository;
    private final FolderPackingListRepository folderPackingListRepository;


    public TogetherListInviteResponseDto getInviteTogetherList(Long userId, String inviteCode) {

        // invitecode로 존재하는 패킹리스트인지 확인
        TogetherPackingList togetherPackingList = validateTogetherPackingInviteCode(togetherPackingListRepository, inviteCode);

        // 이미 추가된 멤버인지 확인
        Optional<UserGroup> userGroup = userGroupRepository.findByGroupAndUserId(togetherPackingList.getGroup(), userId);
        if (userGroup.isPresent()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(togetherPackingList, userId);
            return new TogetherListInviteResponseDto(String.valueOf(togetherAlonePackingList.getId()), true);
        } else {
            return new TogetherListInviteResponseDto(String.valueOf(togetherPackingList.getId()), false);
        }
    }

    public MemberResponseDto addMember(MemberAddDto memberAddDto, Long userId) {
        User user = validateUserId(userRepository, userId);
        TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, Long.parseLong(memberAddDto.getListId()));

        TogetherPackingList togetherPackingList = togetherAlonePackingList.getTogetherPackingList();
        validateTogetherListDeleted(togetherPackingList);

        // 해당 유저가 그룹에 이미 존재하는지 확인
        Group group = togetherPackingList.getGroup();
        validateDuplicatedMember(group, user);

        // user_group 추가
        UserGroup userGroup = new UserGroup(user, group);
        userGroupRepository.save(userGroup);

        // 기본 폴더
        Folder defaultFolder = folderRepository.findByUserIdAndNameAndIsAloned(userId, "기본", false)
                .orElseGet(() -> {
                    Folder folder = new Folder(user, "기본", false);
                    folderRepository.save(folder);
                    return folder;
                });

        // 함께 속 혼자 패킹 생성
        PackingList packingList = togetherPackingList.getPackingList();
        PackingList newPackingList = new PackingList(packingList.getTitle(), packingList.getDepartureDate());
        AlonePackingList myPackingList = new AlonePackingList();
        newPackingList.setAlonePackingList(myPackingList);
        myPackingList.setPackingList(newPackingList);
        myPackingList.setIsAloned(false);
        packingListRepository.save(newPackingList);
        alonePackingListRepository.save(myPackingList);

        // 기본 카테고리
        Category category = new Category(newPackingList, "기본");
        categoryRepository.save(category);

        TogetherAlonePackingList newTogetherAlonePackingList = new TogetherAlonePackingList(togetherPackingList, myPackingList);
        togetherAlonePackingListRepository.save(newTogetherAlonePackingList);

        FolderPackingList folderPackingList = new FolderPackingList(defaultFolder, myPackingList);
        folderPackingListRepository.save(folderPackingList);

        return new MemberResponseDto(newTogetherAlonePackingList.getId().toString());

    }
}
