package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryPackMapping;
import packman.dto.folder.AloneListsInFolderResponseDto;
import packman.dto.folder.FolderIdNameMapping;
import packman.dto.list.ListIdDtoMapping;
import packman.dto.list.ListInFolderDto;
import packman.dto.pack.PackCountMapping;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;

    public List<FolderIdNameMapping> getAloneFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        return folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
    }

    public List<FolderIdNameMapping> getTogetherFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        return folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, false);
    }

    public AloneListsInFolderResponseDto getAloneListsInFolder(Long userId, Long folderId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        // 유저가 해당 폴더를 가지고 있지 않은 경우
        folderRepository.findByIdAndUserId(folderId, userId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER_FOLDER)
        );

        // 폴더가 존재하지 않는 경우, 함께 패킹리스트 폴더인 경우
        FolderIdNameMapping currentFolder = folderRepository.findByIdAndIsAloned(folderId, true).orElseThrow(
                () -> new CustomException(ResponseCode.NO_FOLDER)
        );

        List<FolderIdNameMapping> folders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
        List<ListIdDtoMapping> alonePackingLists =
                folderPackingListRepository.findByFolderIdAndAlonePackingList_IsAlonedOrderByIdDesc(folderId, true);

        String listNum = String.valueOf(alonePackingLists.size());

        List<ListInFolderDto> listInFolderDtos = new ArrayList<>();
        for (ListIdDtoMapping alonePackingList : alonePackingLists) {
            Long id = alonePackingList.getId();

            Optional<PackingList> list = packingListRepository.findById(id);
            if (list.isPresent()) {
                String title = list.get().getTitle();
                String departureDate = String.valueOf(list.get().getDepartureDate());
                long packTotalNum = 0L;
                long packRemainNum = 0L;

                if (list.get().getCategory().size() != 0) {
                    CategoryPackMapping categoryPackMapping = categoryRepository.findByPackingListId(id);

                    for (PackCountMapping packCountMapping : categoryPackMapping.getPack()) {
                        packTotalNum++;

                        if (!packCountMapping.getIsChecked()) {
                            packRemainNum++;
                        }
                    }
                }

                ListInFolderDto listInFolderDto =
                        new ListInFolderDto(String.valueOf(id), title, departureDate, String.valueOf(packTotalNum), String.valueOf(packRemainNum));
                listInFolderDtos.add(listInFolderDto);
            } else {
                throw new CustomException(ResponseCode.NO_LIST);
            }
        }
        return new AloneListsInFolderResponseDto(currentFolder, folders, listNum, listInFolderDtos);
    }
}
