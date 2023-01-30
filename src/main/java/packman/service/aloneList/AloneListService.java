package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.alonelist.AloneListResponseDto;
import packman.dto.folder.FolderIdNameMapping;
import packman.dto.list.ListRequestDto;
import packman.entity.Category;
import packman.entity.Folder;
import packman.entity.FolderPackingList;
import packman.entity.Pack;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.template.TemplateCategory;
import packman.entity.template.TemplatePack;
import packman.repository.*;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.template.TemplateCategoryRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final PackingListRepository packingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final PackRepository packRepository;

    public AloneListResponseDto createAloneList(ListRequestDto listRequestDto, Long userId) {
        Long folderId = Long.parseLong(listRequestDto.getFolderId());
        String title = listRequestDto.getTitle();
        LocalDate departureDate = LocalDate.parse(listRequestDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
        String inviteCode;

        // inviteCode 생성
        do{
            inviteCode = RandomStringUtils.randomAlphanumeric(5);
        }while(alonePackingListRepository.existByInviteCode(inviteCode));

        // 유저 검증
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        // 유저 소유 폴더 X 함께 패킹리스트 폴더 or 존재하지 않는 폴더 X
        Folder folder = folderRepository.findByIdAndUserIdAndIsAloned(folderId, userId, true).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER_FOLDER)
        );

        // 제목 글자수 검증
        if (title.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }

        // 패킹리스트 생성
        PackingList packingList = new PackingList(title, departureDate);
        PackingList savedList = packingListRepository.save(packingList);

        // 혼자 패킹리스트 생성
        AlonePackingList alonePackingList = new AlonePackingList(savedList.getId(), inviteCode);
        AlonePackingList savedAloneList = alonePackingListRepository.save(alonePackingList);

        // 폴더-패킹리스트 저장
        FolderPackingList folderPackingList = new FolderPackingList(folder, savedAloneList);
        folderPackingListRepository.save(folderPackingList);


        // 템플릿 적용
        if(listRequestDto.getTemplateId() == ""){ //템플릿 X
            Category category = new Category(savedList, "기본");
            categoryRepository.save(category);
        }else{ // 템플릿 O
            List<TemplateCategory> categories = templateRepository.findById(Long.parseLong(listRequestDto.getTemplateId())).get().getCategories();
            categories.forEach(m -> {
                Category tempCategory = new Category(savedList, m.getName());
                Category savedCategory = categoryRepository.save(tempCategory);

                List<TemplatePack> packs = templateCategoryRepository.findById(tempCategory.getId()).get().getTemplatePacks();
                packs.forEach(n -> {
                    Pack pack = new Pack(savedCategory, n.getName());
                    packRepository.save(pack);
                } );
            });
        }




















    }
