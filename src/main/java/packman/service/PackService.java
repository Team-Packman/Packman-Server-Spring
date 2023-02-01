package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.ListResponseMapping;
import packman.dto.pack.PackCreateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;

import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateListCategory;
import static packman.validator.Validator.validateUserList;

@Service
@Transactional
@RequiredArgsConstructor
public class PackService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;

    public ListResponseMapping createAlonePack(PackCreateDto packCreateDto, Long userId) {
        Long aloneListId = Long.valueOf(packCreateDto.getListId());

        validateUserId(userRepository, userId);
        String title = validateUserList(folderPackingListRepository, userId, aloneListId);

        addPackInCategory(packCreateDto);

        return packingListRepository.findByIdAndTitle(aloneListId, title);
    }

    public ListResponseMapping createTogetherPack(PackCreateDto packCreateDto, Long userId) {
        Long togetherListId = Long.valueOf(packCreateDto.getListId());

        validateUserId(userRepository, userId);

        addPackInCategory(packCreateDto);

        return packingListRepository.findByIdAndTitle(togetherListId, packingList.getTitle());
    }

    public void addPackInCategory(PackCreateDto packCreateDto) {
        Long listId = Long.valueOf(packCreateDto.getListId());
        Long categoryId = Long.valueOf(packCreateDto.getListId());
        String packName = packCreateDto.getName();

        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(listId, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);
    }
}