package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryUpdateDto;
import packman.dto.list.ListResponseMapping;
import packman.entity.Category;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import static packman.validator.DuplicatedValidator.validateDuplicatedCategory;
import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.IdValidator.validatePackingListIdInUser;
import static packman.validator.LengthValidator.validateCategoryLength;
import static packman.validator.Validator.validateUserAloneList;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListCategoryService {
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;
    private final AlonePackingListRepository alonePackingListRepository;

    public ListResponseMapping createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validateUserAloneList(userId, Long.parseLong(categoryCreateDto.getListId()), alonePackingListRepository, packingListRepository);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryCreateDto.getName(), null);

        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);

        // response
        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());
        return listResponseMapping;
    }

    public ListResponseMapping updateCategory(CategoryUpdateDto categoryUpdateDto, Long userId) {
        // 카테고리 exceed_len
        validateCategoryLength(categoryUpdateDto.getName());

        // no_list
        PackingList packingList = validateUserAloneList(userId, Long.parseLong(categoryUpdateDto.getListId()), alonePackingListRepository, packingListRepository);

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
        ListResponseMapping categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryUpdateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }

    public void deleteCategory(Long listId, Long categoryId, Long userId) {
        // no_list
        PackingList packingList = validateUserAloneList(userId, listId, alonePackingListRepository, packingListRepository);


        // no_user_list
        validatePackingListIdInUser(packingList, userId);

        // no_category
        Category category = validateCategoryId(categoryRepository, categoryId);

        // no_list_category
        if (!category.getPackingList().getId().equals(listId)) {
            throw new CustomException(ResponseCode.NO_LIST_CATEGORY);
        }
        // delete
        categoryRepository.delete(category);

    }
}
