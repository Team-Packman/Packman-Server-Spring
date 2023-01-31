package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryResponseDto;
import packman.dto.category.CategoryUpdateDto;
import packman.entity.Category;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
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

    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryCreateDto.getListId()));

        // no_member_user
        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryCreateDto.getName(), null);

        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);
        packingListRepository.save(packingList);


        // response
        CategoryResponseDto categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }

    public CategoryResponseDto updateCategory(CategoryUpdateDto categoryUpdateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryUpdateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryUpdateDto.getListId()));

        // no_category
        Category category = validateCategoryId(categoryRepository, Long.parseLong(categoryUpdateDto.getId()));

        // no_list_category
        if (category.getPackingList().getId() != Long.parseLong(categoryUpdateDto.getListId())) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }

        // no_member_user
        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryUpdateDto.getName(), Long.parseLong(categoryUpdateDto.getId()));

        // update
        category.setName(categoryUpdateDto.getName());

        // response
        CategoryResponseDto categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryUpdateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }

    public void deleteCategory(Long listId, Long categoryId, Long userId) {
        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, listId);

        // no_member_user
        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        // no_category
        Category category = validateCategoryId(categoryRepository, categoryId);

        // no_list_category
        if (category.getPackingList().getId() != listId) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
        // delete
        categoryRepository.delete(category);

    }

}
