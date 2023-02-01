package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryResponseDto;
import packman.dto.pack.PackCreateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.packingList.AlonePackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;

import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateListCategory;
import static packman.validator.Validator.validateUserList;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListPackService {
    private final UserRepository userRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;

    public CategoryResponseDto createPack(PackCreateDto packCreateDto, Long userId) {
        Long listId = Long.valueOf(packCreateDto.getListId());
        Long categoryId = Long.valueOf(packCreateDto.getCategoryId());
        String packName = packCreateDto.getName();

        validateUserId(userRepository, userId);
        AlonePackingList alonePackingList = validateUserList(folderPackingListRepository, userId, listId);

        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(listId, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);

        return packingListRepository.findByIdAndTitle(listId, alonePackingList.getPackingList().getTitle());
    }
}
