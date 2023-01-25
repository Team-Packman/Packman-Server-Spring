package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.AloneListsInFolderResponseDto;
import packman.dto.folder.FolderIdNameMapping;
import packman.repository.CategoryRepository;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;

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
    }
}
