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
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import static packman.validator.DuplicatedValidator.validateDuplicatedCategory;
import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.IdValidator.validatePackingListId;
import static packman.validator.LengthValidator.validateCategoryLength;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListCategoryService {
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryCreateDto.getListId()));

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

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryUpdateDto.getName(), Long.parseLong(categoryUpdateDto.getId()));


        // update
        category.setName(categoryUpdateDto.getName());
        categoryRepository.save(category);

        // response
        CategoryResponseDto categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryUpdateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }
}
