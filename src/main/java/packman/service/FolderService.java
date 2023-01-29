package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryPackMapping;
import packman.dto.folder.AloneListsInFolderResponseDto;
import packman.dto.folder.FolderAloneListMapping;
import packman.dto.folder.FolderIdNameMapping;
import packman.dto.folder.TogetherListsInFolderResponseDto;
import packman.dto.list.ListInFolderDto;
import packman.dto.list.TogetherAloneListMapping;
import packman.dto.pack.PackCountMapping;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
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
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;

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
        List<FolderAloneListMapping> alonePackingLists = folderPackingListRepository.findByFolderIdAndAlonePackingList_IsAlonedOrderByIdDesc(folderId, true);

        String listNum = String.valueOf(alonePackingLists.size());

        List<ListInFolderDto> listInFolderDtos = new ArrayList<>();
        for (FolderAloneListMapping alonePackingList : alonePackingLists) {
            Long id = alonePackingList.getAlonePackingList().getId();

            addListInFolderDto(listInFolderDtos, id, id);
        }
        return new AloneListsInFolderResponseDto(currentFolder, folders, listNum, listInFolderDtos);
    }
    
    public TogetherListsInFolderResponseDto getTogetherListsInFolder(Long userId, Long folderId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        // 유저가 해당 폴더를 가지고 있지 않은 경우
        folderRepository.findByIdAndUserId(folderId, userId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER_FOLDER)
        );
        // 폴더가 존재하지 않는 경우, 혼자 패킹리스트 폴더인 경우
        FolderIdNameMapping currentFolder = folderRepository.findByIdAndIsAloned(folderId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_FOLDER)
        );

        List<FolderIdNameMapping> folders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, false);
        List<FolderAloneListMapping> myPackingLists = folderPackingListRepository.findByFolderIdAndAlonePackingList_IsAlonedOrderByIdDesc(folderId, false);

        String listNum = String.valueOf(myPackingLists.size());

        List<ListInFolderDto> listInFolderDtos = new ArrayList<>();

        for (FolderAloneListMapping myPackingList : myPackingLists) {
            Long myPackingListId = myPackingList.getAlonePackingList().getId();
            TogetherAloneListMapping togetherAloneListMapping = togetherAlonePackingListRepository.findByAlonePackingListId(myPackingListId);
            Long integratedId = togetherAloneListMapping.getId();
            Long togetherId = togetherAloneListMapping.getTogetherPackingList().getId();

            addListInFolderDto(listInFolderDtos, integratedId, togetherId);
        }
        return new TogetherListsInFolderResponseDto(currentFolder, folders, listNum, listInFolderDtos);
    }

    private void addListInFolderDto(List<ListInFolderDto> listInFolderDtos, Long representativeId, Long listId) {
        PackingList list = packingListRepository.findById(listId).orElseThrow(() -> new CustomException(ResponseCode.NO_LIST));

        String title = list.getTitle();
        String departureDate = String.valueOf(list.getDepartureDate());
        long packTotalNum = 0L;
        long packRemainNum = 0L;

        if (list.getCategory().size() != 0) {
            CategoryPackMapping categoryPackMapping = categoryRepository.findByPackingListId(listId);

            for (PackCountMapping packCountMapping : categoryPackMapping.getPack()) {
                packTotalNum++;

                if (!packCountMapping.getIsChecked()) {
                    packRemainNum++;
                }
            }
        }
        ListInFolderDto listInFolderDto =
                new ListInFolderDto(String.valueOf(representativeId), title, departureDate, String.valueOf(packTotalNum), String.valueOf(packRemainNum));
        listInFolderDtos.add(listInFolderDto);
    }
}
