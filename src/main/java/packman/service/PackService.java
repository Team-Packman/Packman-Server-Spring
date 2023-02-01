package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.ListResponseMapping;
import packman.dto.pack.PackUpdateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.PackRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;

import java.util.List;

import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PackService {
    private final UserRepository userRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final PackingListRepository packingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final PackRepository packRepository;

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

    public PackingList validateList(Long userId, Long togetherListId) {
        PackingList packingList = validatePackingListId(packingListRepository, togetherListId);
        validateTogetherPackingListId(togetherPackingListRepository, togetherListId);

        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        return packingList;
    }
}
