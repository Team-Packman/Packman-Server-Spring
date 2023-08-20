package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.list.ListResponseMapping;
import packman.dto.category.CategoryUpdateDto;
import packman.entity.Category;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.CustomException;
import packman.util.LogMessage;
import packman.util.ResponseCode;

import java.util.List;

import static packman.validator.DuplicatedValidator.validateDuplicatedCategory;
import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateCategoryLength;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListCategoryService {
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;

    public ListResponseMapping createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryCreateDto.getListId()));
        TogetherPackingList togetherPackingList = validateTogetherPackingListId(togetherPackingListRepository, packingList.getId());

        // no_member_user
        List<UserGroup> userGroups = togetherPackingList.getGroup().getUserGroups();
        validateUserGroupUserId(userGroups, userId);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryCreateDto.getName(), null);

        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);
        packingListRepository.save(packingList);


        // response
        ListResponseMapping categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());

        LogMessage.setNonDataLog("함께 패킹리스트 카테고리 생성", userId, "click");

        return categoryResponseDto;
    }

    public ListResponseMapping updateCategory(CategoryUpdateDto categoryUpdateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryUpdateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryUpdateDto.getListId()));
        TogetherPackingList togetherPackingList = validateTogetherPackingListId(togetherPackingListRepository, packingList.getId());

        // no_category
        Category category = validateCategoryId(categoryRepository, Long.parseLong(categoryUpdateDto.getId()));

        // no_list_category
        if (category.getPackingList().getId() != Long.parseLong(categoryUpdateDto.getListId())) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }

        // no_member_user
        List<UserGroup> userGroups = togetherPackingList.getGroup().getUserGroups();
        validateUserGroupUserId(userGroups, userId);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryUpdateDto.getName(), Long.parseLong(categoryUpdateDto.getId()));

        // update
        category.setName(categoryUpdateDto.getName());

        // response
        ListResponseMapping categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryUpdateDto.getListId()), packingList.getTitle());

        LogMessage.setNonDataLog("함께 패킹리스트 카테고리 수정", userId, "click");

        return categoryResponseDto;
    }

    public void deleteCategory(Long listId, Long categoryId, Long userId) {
        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, listId);
        TogetherPackingList togetherPackingList = validateTogetherPackingListId(togetherPackingListRepository, packingList.getId());

        // no_member_user
        List<UserGroup> userGroups = togetherPackingList.getGroup().getUserGroups();
        validateUserGroupUserId(userGroups, userId);

        // no_category
        Category category = validateCategoryId(categoryRepository, categoryId);

        // no_list_category
        if (!category.getPackingList().getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
        // delete
        categoryRepository.delete(category);

        LogMessage.setNonDataLog("함께 패킹리스트 카테고리 삭제", userId, "click");
    }

}
