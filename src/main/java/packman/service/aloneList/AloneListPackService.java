package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryResponseDto;
import packman.dto.pack.PackCreateDto;
import packman.entity.Category;
import packman.entity.packingList.AlonePackingList;
import packman.repository.CategoryRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateListCategory;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListPackService {
    private final UserRepository userRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;

    public CategoryResponseDto createPack(PackCreateDto packCreateDto, Long userId) {
        Long listId = Long.valueOf(packCreateDto.getListId());
        Long categoryId = Long.valueOf(packCreateDto.getCategoryId());
        String packName = packCreateDto.getName();

        validateUserId(userRepository, userId);
        AlonePackingList alonePackingList = validateAloneListId(alonePackingListRepository, listId);
        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(listId, category);
    }
}
