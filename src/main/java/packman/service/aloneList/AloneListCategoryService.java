package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryResponseDto;
import packman.dto.category.CategoryUpdateDto;
import packman.entity.Category;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import static packman.validator.DuplicatedValidator.validateDuplicatedCategory;
import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateCategoryLength;
import static packman.validator.Validator.validateUserAloneList;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListCategoryService {
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;
    private final AlonePackingListRepository alonePackingListRepository;

    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryCreateDto.getListId()));
        validateUserAloneList(userId, validateAlonePackingListId(alonePackingListRepository, packingList.getId()));

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryCreateDto.getName(), null);

        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);

        // response
        CategoryResponseDto categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }

    public CategoryResponseDto updateCategory(CategoryUpdateDto categoryUpdateDto, Long userId) {
        // 카테고리 exceed_len
        validateCategoryLength(categoryUpdateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryUpdateDto.getListId()));
        validateUserAloneList(userId, validateAlonePackingListId(alonePackingListRepository, packingList.getId()));

        // no_category
        Category category = validateCategoryId(categoryRepository, Long.parseLong(categoryUpdateDto.getId()));

        // no_list_category
        if (category.getPackingList().getId() != Long.parseLong(categoryUpdateDto.getListId())) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }

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
        validateUserAloneList(userId, validateAlonePackingListId(alonePackingListRepository, packingList.getId()));

        // no_user_list
        validatePackingListIdInUser(packingList, userId);

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
