package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.ListResponseMapping;
import packman.dto.pack.PackCreateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;

import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateListCategory;
import static packman.validator.Validator.validateUserList;

@Service
@Transactional
@RequiredArgsConstructor
public class ListPackService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;

    public ListResponseMapping createAlonePack(PackCreateDto packCreateDto, Long userId) {
        Long aloneId = Long.valueOf(packCreateDto.getListId());

        validateUserId(userRepository, userId);

        String title = validateUserList(folderPackingListRepository, userId, aloneId);

        addPackInCategory(aloneId, Long.valueOf(packCreateDto.getCategoryId()), packCreateDto.getName());

        return packingListRepository.findByIdAndTitle(aloneId, title);
    }

    public void addPackInCategory(Long listId, Long categoryId, String packName) {
        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(listId, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);
    }
}
