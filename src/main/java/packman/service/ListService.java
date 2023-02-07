package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.User;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.template.Template;
import packman.entity.template.TemplateCategory;
import packman.entity.template.TemplatePack;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
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

    public ListTitleResponseDto updateTitle(ListTitleRequestDto listTitleRequestDto, Long userId) {
        Long listId = Long.parseLong(listTitleRequestDto.getId());
        Long aloneListId = listId;
        String title = listTitleRequestDto.getTitle();

        //유저 검증
        validateUserId(userRepository, userId);

        //제목 글자수 검증
        validateListLength(title);

        if (!listTitleRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_LIST));
            listId = togetherAlonePackingList.getTogetherPackingList().getId();
            aloneListId = togetherAlonePackingList.getAlonePackingList().getId();
        }

        // 유저의 패킹리스트인지 검증
        validateUserList(folderPackingListRepository, userId, aloneListId);

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setTitle(title);
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

        return new ListTitleResponseDto(listTitleRequestDto.getId(), title);
    }

    public DepartureDateResponseDto updateDepartureDate(DepartureDateRequestDto departureDateRequestDto, Long userId) {
        Long listId = Long.parseLong(departureDateRequestDto.getId());
        Long aloneListId = listId;
        LocalDate departureDate = LocalDate.parse(departureDateRequestDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);

        //유저 검증
        validateUserId(userRepository, userId);

        if (!departureDateRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_LIST));
            listId = togetherAlonePackingList.getTogetherPackingList().getId();
            aloneListId = togetherAlonePackingList.getAlonePackingList().getId();
        }

        // 유저의 패킹리스트인지 검증
        validateUserList(folderPackingListRepository, userId, aloneListId);

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setDepartureDate(departureDate);
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

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
        if(templateUpdateDto.getIsAloned()){
            aloneList = validateUserAloneListIsSaved(folderPackingListRepository, userId, Long.parseLong(templateUpdateDto.getId()), templateUpdateDto.getIsSaved());
            title = aloneList.getPackingList().getTitle();
            categories = aloneList.getPackingList().getCategory();
        } else{
            TogetherAlonePackingList togetherAlonePackingList = validateUserTogetherListIsSaved(togetherAlonePackingListRepository, Long.parseLong(templateUpdateDto.getId()), user, templateUpdateDto.getIsSaved());
            title = togetherAlonePackingList.getTogetherPackingList().getPackingList().getTitle();
            categories = togetherAlonePackingList.getTogetherPackingList().getPackingList().getCategory();
            aloneList = togetherAlonePackingList.getAlonePackingList();
        }

        if(!templateUpdateDto.getIsSaved()){ // 템플릿을 처음 생성하는 경우
            // isSaved 템플릿 저장으로 변경
            aloneList.getPackingList().setIsSaved(true);
            //템플릿 생성
            template = templateRepository.save(new Template(templateUpdateDto.getIsAloned(), title, aloneList, user));
        } else{ // 이미 템플릿이 존재하는 경우
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

        UpdatedTemplateResponseDto updatedTemplateResponseDto = UpdatedTemplateResponseDto.builder()
                .id(templateUpdateDto.getId())
                .isSaved(aloneList.getPackingList().getIsSaved()).build();

        return updatedTemplateResponseDto;
    }
}
