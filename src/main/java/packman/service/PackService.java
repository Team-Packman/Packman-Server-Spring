package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.ListResponseMapping;
import packman.dto.pack.PackCreateDto;
import packman.dto.pack.PackUpdateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.PackRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.LogMessage;
import packman.validator.Validator;

import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PackService {
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final PackRepository packRepository;

    public ListResponseMapping createAlonePack(PackCreateDto packCreateDto, Long userId) {
        Long aloneListId = Long.valueOf(packCreateDto.getListId());

        PackingList packingList = validateUserAloneList(userId, aloneListId, alonePackingListRepository, packingListRepository);

        addPackInCategory(packCreateDto, packingList);

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(aloneListId, packingList.getTitle());

        LogMessage.setNonDataLog("패킹리스트 수정", userId);

        return listResponseMapping;
    }

    public ListResponseMapping createTogetherPack(PackCreateDto packCreateDto, Long userId) {
        Long togetherListId = Long.valueOf(packCreateDto.getListId());

        PackingList packingList = validateTogetherList(userId, togetherListId, packingListRepository, togetherPackingListRepository);

        addPackInCategory(packCreateDto, packingList);

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(togetherListId, packingList.getTitle());

        LogMessage.setNonDataLog("패킹리스트 수정", userId);

        return listResponseMapping;
    }

    public void addPackInCategory(PackCreateDto packCreateDto, PackingList packingList) {
        Long categoryId = Long.valueOf(packCreateDto.getCategoryId());
        String packName = packCreateDto.getName();

        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(packingList, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);
    }

    public ListResponseMapping updateAlonePack(PackUpdateDto packUpdateDto, Long userId) {
        Long aloneListId = Long.valueOf(packUpdateDto.getListId());

        PackingList packingList = validateUserAloneList(userId, aloneListId, alonePackingListRepository, packingListRepository);

        updatePackInCategory(packUpdateDto, packingList);

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(aloneListId, packingList.getTitle());

        LogMessage.setNonDataLog("패킹리스트 수정", userId);

        return listResponseMapping;
    }

    public ListResponseMapping updateTogetherPack(PackUpdateDto packUpdateDto, Long userId) {
        Long togetherListId = Long.valueOf(packUpdateDto.getListId());

        PackingList packingList = Validator.validateTogetherList(userId, togetherListId, packingListRepository, togetherPackingListRepository);

        updatePackInCategory(packUpdateDto, packingList);

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(togetherListId, packingList.getTitle());

        LogMessage.setNonDataLog("패킹리스트 수정", userId);

        return listResponseMapping;
    }

    public void updatePackInCategory(PackUpdateDto packUpdateDto, PackingList packingList) {
        Long categoryId = Long.valueOf(packUpdateDto.getCategoryId());
        String packName = packUpdateDto.getName();
        Long packId = Long.valueOf(packUpdateDto.getId());

        validatePackLength(packName);
        Pack pack = validateListCategoryPack(packingList, categoryId, packId, categoryRepository, packRepository);

        pack.setChecked(packUpdateDto.getIsChecked());
        pack.setName(packName);
    }

    public void deleteAlonePack(Long listId, Long categoryId, Long packId, Long userId) {
        PackingList packingList = validateUserAloneList(userId, listId, alonePackingListRepository, packingListRepository);

        packRepository.delete(validateListCategoryPack(packingList, categoryId, packId, categoryRepository, packRepository));

        LogMessage.setNonDataLog("패킹리스트 수정", userId);
    }

    public void deleteTogetherPack(Long listId, Long categoryId, Long packId, Long userId) {
        PackingList packingList = validateTogetherList(userId, listId, packingListRepository, togetherPackingListRepository);

        packRepository.delete(validateListCategoryPack(packingList, categoryId, packId, categoryRepository, packRepository));

        LogMessage.setNonDataLog("패킹리스트 수정", userId);
    }
}