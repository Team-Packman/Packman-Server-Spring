package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.dto.log.AloneListLogDto;
import packman.entity.Category;
import packman.entity.Folder;
import packman.entity.FolderPackingList;
import packman.entity.Pack;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.template.*;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.basicTemplate.BasicTemplateCategoryRepository;
import packman.repository.basicTemplate.BasicTemplateRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.template.TemplateCategoryRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.LogMessage;
import packman.util.ResponseCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.*;


@Service
@Transactional
@RequiredArgsConstructor
public class AloneListService {
    private final FolderRepository folderRepository;
    private final PackingListRepository packingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final BasicTemplateRepository basicTemplateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final BasicTemplateCategoryRepository basicTemplateCategoryRepository;


    public AloneListResponseDto createAloneList(ListCreateDto listCreateDto, Long userId) {
        Long folderId = Long.parseLong(listCreateDto.getFolderId());
        String title = listCreateDto.getTitle();
        LocalDate departureDate = LocalDate.parse(listCreateDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
        Boolean isBasic = listCreateDto.getIsBasic();
        String inviteCode;

        // inviteCode 생성
        do {
            inviteCode = RandomStringUtils.randomAlphanumeric(5);
        } while (alonePackingListRepository.existsByInviteCode(inviteCode));

        // 유저 소유 폴더 X 함께 패킹리스트 폴더 or 존재하지 않는 폴더의 경우
        Folder folder = validateUserFolder(folderRepository, folderId, userId, true);

        // 제목 글자수 검증
        validateListLength(title);

        // 패킹리스트 생성
        PackingList savedList = packingListRepository.save(new PackingList(title, departureDate));

        // 혼자 패킹리스트 생성
        AlonePackingList savedAloneList = alonePackingListRepository.save(new AlonePackingList(savedList, inviteCode));


        // 폴더-패킹리스트 저장
        folderPackingListRepository.save(new FolderPackingList(folder, savedAloneList));


        // 템플릿 적용
        if (listCreateDto.getTemplateId().equals("")) { //템플릿 X
            savedList.addCategory(new Category(savedList, "기본"));
        } else { // 템플릿 O
            if (isBasic) {
                // 기본 템플릿
                BasicTemplate template = validateBasicTemplateId(basicTemplateRepository, Long.parseLong(listCreateDto.getTemplateId()));

                List<BasicTemplateCategory> categories = template.getCategories();
                categories.forEach(m -> {
                    Category savedCategory = categoryRepository.save(new Category(savedList, m.getName()));
                    savedList.addCategory(savedCategory);

                    List<BasicTemplatePack> packs = basicTemplateCategoryRepository.findById(m.getId()).get().getTemplatePacks();
                    packs.forEach(n -> {
                        savedCategory.addPack(new Pack(savedCategory, n.getName()));
                    });
                });
            } else {
                // 나만의 템플릿
                Template template = validateTemplateId(templateRepository, Long.parseLong(listCreateDto.getTemplateId()));

                List<TemplateCategory> categories = template.getCategories();
                categories.forEach(m -> {
                    Category savedCategory = categoryRepository.save(new Category(savedList, m.getName()));
                    savedList.addCategory(savedCategory);

                    List<TemplatePack> packs = templateCategoryRepository.findById(m.getId()).get().getTemplatePacks();
                    packs.forEach(n -> {
                        savedCategory.addPack(new Pack(savedCategory, n.getName()));
                    });
                });
            }
        }
        ListResponseMapping savedCategories = packingListRepository.findByIdAndTitle(savedList.getId(), savedList.getTitle());

        // 로그
        AloneListLogDto aloneListLogDto = AloneListLogDto.builder()
                .id(savedList.getId().toString())
                .templateId(listCreateDto.getTemplateId())
                .title(savedList.getTitle())
                .departureDate(savedList.getDepartureDate().toString())
                .category(savedCategories.getCategory())
                .build();

        LogMessage.setDataLog("혼자 패킹리스트 생성", aloneListLogDto, userId);

        return AloneListResponseDto.builder()
                .id(Long.toString(savedList.getId()))
                .title(savedList.getTitle())
                .departureDate(savedList.getDepartureDate().toString())
                .category(savedCategories.getCategory())
                .inviteCode(savedAloneList.getInviteCode())
                .isSaved(savedList.getIsSaved()).build();
    }

    public DetailedAloneListResponseDto getAloneList(Long listId, Long userId, String inviteCode) {

        // 생성자가 본인 패킹리스트 조회
        if (inviteCode == null) {
            // 유저의 혼자 패킹리스트인지 검증
            FolderPackingList folderPackingList = validateUserAloneListId(folderPackingListRepository, userId, listId);

            ListResponseMapping categories = packingListRepository.findProjectionById(listId);

            return DetailedAloneListResponseDto.builder()
                    .id(folderPackingList.getAlonePackingList().getId().toString())
                    .folderId(folderPackingList.getFolder().getId().toString())
                    .category(categories.getCategory())
                    .inviteCode(folderPackingList.getAlonePackingList().getInviteCode())
                    .isSaved(folderPackingList.getAlonePackingList().getPackingList().getIsSaved()).build();
        }

        // 초대코드로 패킹리스트 조회
        AlonePackingList alonePackingList = validateAlonePackingListByInviteCode(alonePackingListRepository, inviteCode);

        if (!alonePackingList.getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST);
        }

        ListResponseMapping categories = packingListRepository.findProjectionById(listId);

        return DetailedAloneListResponseDto.builder()
                .id(alonePackingList.getId().toString())
                .folderId(alonePackingList.getFolderPackingList().getFolder().getId().toString())
                .category(categories.getCategory())
                .inviteCode(inviteCode)
                .isSaved(alonePackingList.getPackingList().getIsSaved()).build();
    }

    public void deleteAloneList(Long userId, Long folderId, List<Long> listIds) {

        // 유저 소유 폴더, 혼자 패킹리스트 폴더인지 검증
        validateUserFolder(folderRepository, folderId, userId, true);

        // 혼자 패킹 리스트, 존재하는 리스트인지 검증
        List<AlonePackingList> alonePackingLists = validateAloneListIds(alonePackingListRepository, listIds);

        // 해당 리스트가 폴더 속에 있는지 검증
        List<FolderPackingList> folderPackingLists = validateFolderLists(folderPackingListRepository, folderId, listIds);

        // 삭제할 패킹리스트 취합
        List<PackingList> lists = alonePackingLists.stream().map(aloneList -> aloneList.getPackingList()).collect(Collectors.toList());

        // 삭제할 패킹리스트 isDeleted true 처리
        packingListRepository.updateListIsDeletedTrue(lists);

        // 폴더-패킹리스트 튜플 삭제
        folderPackingListRepository.deleteAllInBatch(folderPackingLists);
    }

    public InviteAloneListResponseDto getInviteAloneList(Long userId, String inviteCode) {
        AlonePackingList alonePackingList = validateAlonePackingListByInviteCode(alonePackingListRepository, inviteCode);
        Long ownerId = alonePackingList.getFolderPackingList().getFolder().getUser().getId();

        boolean isOwner = ownerId.equals(userId);

        InviteAloneListResponseDto inviteAloneListResponseDto = InviteAloneListResponseDto.builder()
                .id(alonePackingList.getId().toString())
                .IsOwner(isOwner)
                .build();

        LogMessage.setDataLog("혼자 패킹리스트 초대", inviteAloneListResponseDto, userId);

        return inviteAloneListResponseDto;
    }
}
