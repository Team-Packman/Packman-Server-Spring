package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.entity.template.Template;
import packman.entity.template.TemplateCategory;
import packman.entity.template.TemplatePack;
import packman.repository.*;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateCategoryRepository;
import packman.repository.template.TemplateRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.validateUserFolder;
import static packman.validator.Validator.validateUserList;

import packman.dto.togetherList.TogetherListInviteResponseDto;
import packman.entity.UserGroup;
import packman.repository.UserGroupRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final PackingListRepository packingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final PackRepository packRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;

    public TogetherListResponseDto createTogetherList(ListCreateDto listCreateDto, Long userId) {
        Long folderId = Long.parseLong(listCreateDto.getFolderId());
        String title = listCreateDto.getTitle();
        LocalDate departureDate = LocalDate.parse(listCreateDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
        String inviteCode;

        // inviteCode 생성
        do {
            inviteCode = RandomStringUtils.randomAlphanumeric(5);
        } while (togetherPackingListRepository.existsByInviteCode(inviteCode));

        // 유저 검증
        User user = validateUserId(userRepository, userId);

        // 유저 소유 폴더 X 혼자 패킹리스트 폴더 or 존재하지 않는 폴더의 경우
        Folder folder = validateUserFolder(folderRepository, folderId, userId, false);

        // 제목 글자수 검증
        validateListLength(title);

        // 패킹리스트 생성
        List<PackingList> packingLists = new ArrayList<>();
        packingLists.add(new PackingList(title, departureDate));
        packingLists.add(new PackingList(title, departureDate));

        List<PackingList> savedLists = packingListRepository.saveAll(packingLists);
        PackingList savedTogetherList = savedLists.get(0); // (함께)패킹리스트
        PackingList savedMyList = savedLists.get(1); // (나의)패킹리스트

        // 그룹 생성
        Group savedGroup = groupRepository.save(new Group());

        // 유저-그룹 생성
        userGroupRepository.save(new UserGroup(user, savedGroup));

        // 함께 패킹리스트 생성
        TogetherPackingList savedTogetherPackingList = togetherPackingListRepository.save(new TogetherPackingList(savedTogetherList, savedGroup, inviteCode));

        // 나의 패킹리스트 생성
        AlonePackingList savedMyPackingList = alonePackingListRepository.save(new AlonePackingList(savedMyList, false));

        // 폴더-패킹 리스트 생성
        folderPackingListRepository.save(new FolderPackingList(folder, savedMyPackingList));

        // 함께-나의 패킹리스트 생성
        TogetherAlonePackingList savedTogetherAloneList = togetherAlonePackingListRepository.save(new TogetherAlonePackingList(savedTogetherPackingList, savedMyPackingList));

        // 나의 패킹리스트에 기본 카테고리 추가
        savedMyList.addCategory(new Category(savedMyList, "기본"));

        // 템플릿 적용
        if (listCreateDto.getTemplateId().equals("")) { //템플릿 X
            savedTogetherList.addCategory(new Category(savedTogetherList, "기본"));
        } else { // 템플릿 O
            // 해당 템플릿이 존재하지 않는 경우
            Template template = validateTemplateId(templateRepository, Long.parseLong(listCreateDto.getTemplateId()));

            List<TemplateCategory> categories = template.getCategories();
            categories.forEach(m -> {
                Category savedCategory = categoryRepository.save(new Category(savedTogetherList, m.getName()));
                savedTogetherList.addCategory(savedCategory);

                List<TemplatePack> packs = templateCategoryRepository.findById(m.getId()).get().getTemplatePacks();
                packs.forEach(n -> {
                    savedCategory.addPack(new Pack(savedCategory, n.getName()));
                });
            });
        }

        ListResponseMapping savedMyIdCategories = packingListRepository.findByIdAndTitle(savedMyList.getId(), savedMyList.getTitle());
        ListResponseMapping savedTogetherCategories = packingListRepository.findByIdAndTitle(savedTogetherList.getId(), savedTogetherList.getTitle());

        TogetherListDto togetherListDto = TogetherListDto.builder()
                .id(Long.toString(savedTogetherPackingList.getId()))
                .groupId(Long.toString(savedTogetherPackingList.getGroup().getId()))
                .category(savedTogetherCategories.getCategory())
                .inviteCode(savedTogetherPackingList.getInviteCode())
                .isSaved(savedTogetherList.getIsSaved()).build();

        TogetherListResponseDto togetherListResponseDto = TogetherListResponseDto.builder()
                .id(Long.toString(savedTogetherAloneList.getId()))
                .title(savedTogetherList.getTitle())
                .departureDate(savedTogetherList.getDepartureDate().toString())
                .togetherPackingList(togetherListDto)
                .myPackingList(savedMyIdCategories).build();

        return togetherListResponseDto;
    }

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

    public DetaildTogetherListResponseDto getTogetherList(Long listId, Long userId) {
        // 유저 검증
        validateUserId(userRepository, userId);
        // 존재하는 함께 패킹리스트인지 검증
        TogetherAlonePackingList togetherAloneList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId);
        // 유저의 함께 패킹리스트인지 검증 및 folderId 찾기
        FolderPackingList folderPackingList = validateUserList(folderPackingListRepository, userId, togetherAloneList.getAlonePackingList().getId());

        ListResponseMapping myIdCategories = packingListRepository.findProjectionById(togetherAloneList.getAlonePackingList().getId());
        ListResponseMapping togetherIdCategories = packingListRepository.findProjectionById(togetherAloneList.getTogetherPackingList().getId());

        TogetherListDto togetherListDto = TogetherListDto.builder()
                .id(Long.toString(togetherAloneList.getTogetherPackingList().getId()))
                .groupId(Long.toString(togetherAloneList.getTogetherPackingList().getGroup().getId()))
                .category(togetherIdCategories.getCategory())
                .inviteCode(togetherAloneList.getTogetherPackingList().getInviteCode())
                .isSaved(togetherAloneList.getTogetherPackingList().getPackingList().getIsSaved()).build();

        DetaildTogetherListResponseDto detaildTogetherListResponseDto= DetaildTogetherListResponseDto.builder()
                .id(togetherAloneList.getId().toString())
                .folderId(folderPackingList.getFolder().getId().toString())
                .togetherPackingList(togetherListDto)
                .myPackingList(myIdCategories)
                .isMember(userGroupRepository.existsByGroupAndUserId(togetherAloneList.getTogetherPackingList().getGroup(), userId)).build();

        return detaildTogetherListResponseDto;
    }

}
