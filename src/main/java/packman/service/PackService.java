package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.ListResponseMapping;
import packman.dto.pack.PackCreateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PackService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;

    public ListResponseMapping createAlonePack(PackCreateDto packCreateDto, Long userId) {
        Long aloneListId = Long.valueOf(packCreateDto.getListId());

        validateUserId(userRepository, userId);

        PackingList packingList = validatePackingListId(packingListRepository, aloneListId);
        validateUserAloneList(userId, validateAlonePackingListId(alonePackingListRepository, aloneListId));

        addPackInCategory(packCreateDto, packingList);

        return packingListRepository.findByIdAndTitle(aloneListId, packingList.getTitle());
    }

    public ListResponseMapping createTogetherPack(PackCreateDto packCreateDto, Long userId) {
        Long togetherListId = Long.valueOf(packCreateDto.getListId());

        validateUserId(userRepository, userId);
        PackingList packingList = validateTogetherList(userId, togetherListId, packingListRepository, togetherPackingListRepository);

        addPackInCategory(packCreateDto, packingList);

        return packingListRepository.findByIdAndTitle(togetherListId, packingList.getTitle());
    }

    public void addPackInCategory(PackCreateDto packCreateDto, PackingList packingList) {
        Long categoryId = Long.valueOf(packCreateDto.getListId());
        String packName = packCreateDto.getName();

        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(packingList, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);
    }

    public ListResponseMapping updateAlonePack(PackUpdateDto packUpdateDto, Long userId) {
        Long aloneListId = Long.valueOf(packUpdateDto.getListId());

        validateUserId(userRepository, userId);
        String title = validateUserList(folderPackingListRepository, userId, aloneListId);

        updatePackInCategory(packUpdateDto);

        return packingListRepository.findByIdAndTitle(aloneListId, title);
    }

    public ListResponseMapping updateTogetherPack(PackUpdateDto packUpdateDto, Long userId) {
        Long togetherListId = Long.valueOf(packUpdateDto.getListId());

        validateUserId(userRepository, userId);
        PackingList packingList = validateList(userId, togetherListId);

        updatePackInCategory(packUpdateDto);

        return packingListRepository.findByIdAndTitle(togetherListId, packingList.getTitle());
    }

    public void updatePackInCategory(PackUpdateDto packUpdateDto) {
        Long aloneId = Long.valueOf(packUpdateDto.getListId());
        Long categoryId = Long.valueOf(packUpdateDto.getCategoryId());
        String packName = packUpdateDto.getName();
        Long packId = Long.valueOf(packUpdateDto.getId());

        Category category = validateCategoryId(categoryRepository, categoryId);
        Pack pack = validatePackId(packRepository, packId);

        validatePackLength(packName);

        validateListCategory(aloneId, category);
        validateCategoryPack(categoryId, pack);

        pack.setChecked(packUpdateDto.getIsChecked());
        pack.setName(packName);
    }
}