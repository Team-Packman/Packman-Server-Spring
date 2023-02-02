package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryResponseDto;
import packman.dto.list.ListDto;
import packman.dto.list.TogetherListDto;
import packman.dto.list.TogetherListResponseDto;
import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
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

    public TogetherListResponseDto createTogetherList(ListDto listDto, Long userId) {
        Long folderId = Long.parseLong(listDto.getFolderId());
        String title = listDto.getTitle();
        LocalDate departureDate = LocalDate.parse(listDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
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
        PackingList savedTogetherList = savedLists.get(0); // 함께 패킹리스트
        PackingList savedMyList = savedLists.get(1); // 나의 패킹리스트

        // 그룹 생성
        Group group = new Group();
        Group savedGroup = groupRepository.save(group);

        // 유저-그룹 생성
        UserGroup userGroup = new UserGroup(user, savedGroup);
        userGroupRepository.save(userGroup);

        // 함께 패킹리스트 생성
        TogetherPackingList togetherList = new TogetherPackingList(savedTogetherList, savedGroup, inviteCode);
        TogetherPackingList savedTogetherPackingList = togetherPackingListRepository.save(togetherList);

        // 나의 패킹리스트 생성
        AlonePackingList aloneList = new AlonePackingList(savedMyList, false);
        AlonePackingList savedMyPackingList = alonePackingListRepository.save(aloneList);

        // 폴더-패킹 리스트 생성
        FolderPackingList folderList = new FolderPackingList(folder, savedMyPackingList);
        folderPackingListRepository.save(folderList);

        // 함께-나의 패킹리스트 생성
        TogetherAlonePackingList togetherAloneList = new TogetherAlonePackingList(savedTogetherPackingList, savedMyPackingList);
        TogetherAlonePackingList savedTogetherAloneList = togetherAlonePackingListRepository.save(togetherAloneList);

        // 나의 패킹리스트에 기본 카테고리 추가
        Category myCategory = new Category(savedMyList, "기본");
        savedMyList.addCategory(myCategory);
        categoryRepository.save(myCategory);

        // 템플릿 적용
        if (listDto.getTemplateId() == "") { //템플릿 X
            Category category = new Category(savedTogetherList, "기본");
            savedTogetherList.addCategory(category);
            categoryRepository.save(category);
        } else { // 템플릿 O
            List<TemplateCategory> categories = templateRepository.findById(Long.parseLong(listDto.getTemplateId())).get().getCategories();
            categories.forEach(m -> {
                Category tempCategory = new Category(savedTogetherList, m.getName());
                savedTogetherList.addCategory(tempCategory);
                Category savedCategory = categoryRepository.save(tempCategory);

                List<TemplatePack> packs = templateCategoryRepository.findById(m.getId()).get().getTemplatePacks();
                packs.forEach(n -> {
                    Pack pack = new Pack(savedCategory, n.getName());
                    savedCategory.addPack(pack);
                    packRepository.save(pack);
                });
            });
        }

        CategoryResponseDto savedMyIdCategories = packingListRepository.findByIdAndTitle(savedMyList.getId(), savedMyList.getTitle());
        CategoryResponseDto savedTogetherCategories = packingListRepository.findByIdAndTitle(savedTogetherList.getId(), savedTogetherList.getTitle());
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
}
