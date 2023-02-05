package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryResponseDto;
import packman.dto.list.ListCreateDto;
import packman.dto.list.TogetherListDto;
import packman.dto.list.TogetherListResponseDto;
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
import java.util.stream.Collectors;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.validateFolderLists;
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

    public void deleteTogetherList(Long userId, Long folderId, List<Long> listIds) {
        List<Pack> packs = new ArrayList<>();
        List<UserGroup> userGroups = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<PackingList> lists = new ArrayList<>(); //최종적으로 삭제할 packingList들을 담는 리스트

        // 존재하는 유저인지 검증
        validateUserId(userRepository, userId);

        // 유저 소유 폴더, 함께 패킹리스트 폴더인지 검증
        validateUserFolder(folderRepository, folderId, userId, false);

        // 함께 패킹 리스트, 존재하는 리스트인지 검증
        List<TogetherAlonePackingList> togetherAlonePackingLists = validateTogetherListIds(togetherAlonePackingListRepository, listIds);

        // 다음 검증을 위해 혼자 패킹리스트 id 담기
        List<Long> aloneListIds = togetherAlonePackingLists.stream().map(linkList -> linkList.getAlonePackingList().getId()).collect(Collectors.toList());

        // 해당 리스트가 폴더 속에 있는지 검증
        List<FolderPackingList> folderPackingLists = validateFolderLists(folderPackingListRepository, folderId, aloneListIds);

        togetherAlonePackingLists.forEach(linkList -> {
            TogetherPackingList togetherList = linkList.getTogetherPackingList();

            // packer == user인 경우 취합
            packs.addAll(packRepository.findByCategory_PackingListAndPackerId(togetherList.getPackingList(), userId));

            // 삭제할 유저-그룹(본인과 리스트 간 유저-그룹) 취합
            userGroups.add(userGroupRepository.findByUserIdAndGroup(userId, togetherList.getGroup()));

            //삭제할 혼자 패킹리스트 취합
            lists.add(linkList.getAlonePackingList().getPackingList());

            //함께 패킹리스트 그룹의 유저-그룹 수가 1인(그룹에 본인만 존재) 패킹리스트 선별 == 함께 패킹리스트까지 삭제해야함
            if(userGroupRepository.findByGroup(togetherList.getGroup()).size() == 1){
                // 그룹을 삭제해야 하기에 삭제할 그룹 취합
                groups.add(togetherList.getGroup());
                // 삭제할 함께 패킹리스트 추가
                lists.add(togetherList.getPackingList());
            }
        });

        // 유저-그룹 삭제
        userGroupRepository.deleteAllInBatch(userGroups);

        // 폴더-패킹리스트 삭제
        folderPackingListRepository.deleteAllInBatch(folderPackingLists);

        // 함께-혼자 패킹리스트 삭제
        togetherAlonePackingListRepository.deleteAllInBatch(togetherAlonePackingLists);

        // 패킹리스트 isDeleted 처리
        packingListRepository.UpdatelistIsDeletedTrue(lists);

        // packer null 설정
        if(!packs.isEmpty()){packRepository.updatePackerNull(packs);}

        if(!groups.isEmpty()){
            // 함께 패킹리스트의 groupId null로 set
            togetherPackingListRepository.updateGroupNull(groups);
            // 그룹 삭제
            groupRepository.deleteAllInBatch(groups);}
    }
}
