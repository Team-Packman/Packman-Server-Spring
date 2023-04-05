package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.User;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.entity.template.Template;
import packman.entity.template.TemplateCategory;
import packman.entity.template.TemplatePack;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateCategoryRepository;
import packman.repository.template.TemplateRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ListService {
    private final UserRepository userRepository;
    private final PackingListRepository listRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;

    public ListTitleResponseDto updateTitle(ListTitleRequestDto listTitleRequestDto, Long userId) {
        Long listId = Long.parseLong(listTitleRequestDto.getId());
        String title = listTitleRequestDto.getTitle();
        List<AlonePackingList> myPackingLists = new ArrayList<>();
        //제목 글자수 검증
        validateListLength(title);

        if (!listTitleRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId);
            TogetherPackingList togetherPackingList = togetherAlonePackingList.getTogetherPackingList();
            listId = togetherPackingList.getId();
            myPackingLists = alonePackingListRepository.findByTogetherAlonePackingList_TogetherPackingList(togetherPackingList);
        } else {
            // 유저의 패킹리스트인지 검증
            validateUserList(folderPackingListRepository, userId, listId);
        }

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setTitle(title);
        }, () -> {
            throw new CustomException(ResponseCode.NO_LIST);
        });

        for (AlonePackingList myPackingList : myPackingLists) {
            listRepository.findByIdAndIsDeleted(myPackingList.getId(), false).ifPresent(t -> {
                t.setTitle(title);
            });
        }
        return new ListTitleResponseDto(listTitleRequestDto.getId(), title);
    }

    public DepartureDateResponseDto updateDepartureDate(DepartureDateRequestDto departureDateRequestDto, Long userId) {
        Long listId = Long.parseLong(departureDateRequestDto.getId());
        LocalDate departureDate = LocalDate.parse(departureDateRequestDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
        List<AlonePackingList> myPackingLists = new ArrayList<>();

        if (!departureDateRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId);
            TogetherPackingList togetherPackingList = togetherAlonePackingList.getTogetherPackingList();
            listId = togetherPackingList.getId();
            myPackingLists = alonePackingListRepository.findByTogetherAlonePackingList_TogetherPackingList(togetherPackingList);
        } else {
            // 유저의 패킹리스트인지 검증
            validateUserList(folderPackingListRepository, userId, listId);
        }

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setDepartureDate(departureDate);
        }, () -> {
            throw new CustomException(ResponseCode.NO_LIST);
        });

        for (AlonePackingList myPackingList : myPackingLists) {
            listRepository.findByIdAndIsDeleted(myPackingList.getId(), false).ifPresent(t -> {
                t.setDepartureDate(departureDate);
            });
        }
        return new DepartureDateResponseDto(departureDateRequestDto.getId(), departureDateRequestDto.getDepartureDate());
    }

    public UpdatedTemplateResponseDto updateMyTemplate(TemplateUpdateDto templateUpdateDto, Long userId) {
        AlonePackingList aloneList;
        String title;
        Template template;
        List<Category> categories;

        //유저 검증
        User user = validateUserId(userRepository, userId);

        // 업데이트에 사용할 aloneList, title
        if (templateUpdateDto.getIsAloned()) {
            aloneList = validateUserAloneListIsSaved(folderPackingListRepository, userId, Long.parseLong(templateUpdateDto.getId()), templateUpdateDto.getIsSaved());
            title = aloneList.getPackingList().getTitle();
            categories = aloneList.getPackingList().getCategory();
        } else {
            TogetherAlonePackingList togetherAlonePackingList = validateUserTogetherListIsSaved(togetherAlonePackingListRepository, Long.parseLong(templateUpdateDto.getId()), user, templateUpdateDto.getIsSaved());
            title = togetherAlonePackingList.getTogetherPackingList().getPackingList().getTitle();
            categories = togetherAlonePackingList.getTogetherPackingList().getPackingList().getCategory();
            aloneList = togetherAlonePackingList.getAlonePackingList();
        }

        if (!templateUpdateDto.getIsSaved()) { // 템플릿을 처음 생성하는 경우
            // isSaved 템플릿 저장으로 변경
            aloneList.getPackingList().setIsSaved(true);
            //템플릿 생성
            template = templateRepository.save(new Template(templateUpdateDto.getIsAloned(), title, aloneList, user));
        } else { // 이미 템플릿이 존재하는 경우
            template = validateListTemplate(templateRepository, aloneList);
            // 템플릿 이름 업데이트
            template.setTitle(title);
            //부모-자식 관계 끊기

            List<TemplateCategory> deleteTemplateCategories = template.getCategories();
            template.setCategories(new ArrayList<>());
            // 기존 템플릿 카테고리 삭제
            templateCategoryRepository.deleteAll(deleteTemplateCategories);
        }

        categories.forEach(m -> {
            TemplateCategory savedTemplateCategory = templateCategoryRepository.save(new TemplateCategory(template, m.getName()));
            template.addTemplateCategory(savedTemplateCategory);

            List<Pack> packs = categoryRepository.findById(m.getId()).get().getPack();
            packs.forEach(n -> {
                savedTemplateCategory.addTemplatePack(new TemplatePack(savedTemplateCategory, n.getName()));
            });
        });

        return UpdatedTemplateResponseDto.builder()
                .id(templateUpdateDto.getId())
                .isSaved(aloneList.getPackingList().getIsSaved()).build();
    }

    public ListResponseDto getPackingListTitleAndDate(Long listId, boolean isAloned, Long userId) {

        if (!isAloned) {
            TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, listId);
            listId = togetherAlonePackingList.getAlonePackingList().getId();
        }

        AlonePackingList alonePackingList = validateAlonePackingListId(alonePackingListRepository, listId);
        PackingList packingList = alonePackingList.getPackingList();
        validateUserList(folderPackingListRepository, userId, packingList.getId());

        return ListResponseDto.builder()
                .id(packingList.getId().toString())
                .title(packingList.getTitle())
                .departureDate(packingList.getDepartureDate().toString()).build();
    }

    public InviteListResponseDto getInviteList(String listType, String inviteCode) {
        Long listId;
        String title, departureDate;

        if (listType.equals("alone")) {
            AlonePackingList alonePackingList = validateAlonePackingListByInviteCode(alonePackingListRepository, inviteCode);
            listId = alonePackingList.getId();
            title = alonePackingList.getPackingList().getTitle();
            departureDate = alonePackingList.getPackingList().getDepartureDate().toString();
        } else if (listType.equals("together")) {
            TogetherPackingList togetherPackingList = validateTogetherPackingListByInviteCode(togetherPackingListRepository, inviteCode);
            listId = togetherPackingList.getId();
            title = togetherPackingList.getPackingList().getTitle();
            departureDate = togetherPackingList.getPackingList().getDepartureDate().toString();
        } else {
            throw new CustomException(ResponseCode.INVALID_LIST_TYPE);
        }

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(listId, title);

        return InviteListResponseDto.builder()
                .id(listResponseMapping.getId())
                .title(title)
                .departureDate(departureDate)
                .category(listResponseMapping.getCategory())
                .build();
    }
}
