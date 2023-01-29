package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryResponseDto;
import packman.entity.Category;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListCategoryService {
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        if (categoryCreateDto.getName().length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LEN);
        }
        // no_list
        PackingList packingList = packingListRepository.findByIdAndIsDeleted(Long.parseLong(categoryCreateDto.getListId()), false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_LIST)
        );
        // duplicate_category
        List<Category> categorys = packingList.getCategory();
        categorys.stream().forEach(category -> {
            if (category.getName().equals(categoryCreateDto.getName())) {
                throw new CustomException(ResponseCode.DUPLICATED_CATEGORY);
            }
        });
        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);
        packingListRepository.save(packingList);


        // response
        CategoryResponseDto categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }
}
