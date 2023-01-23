package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.AloneFoldersResponseDto;
import packman.dto.folder.FolderIdNameMapping;
import packman.dto.folder.TogetherFoldersResponseDto;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public AloneFoldersResponseDto getAloneFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        ArrayList<FolderIdNameMapping> folderIdNameMappings = folderRepository.findByUserIdAndIsAlonedOrderByCreatedAtDesc(userId, true);

        return new AloneFoldersResponseDto(folderIdNameMappings);
    }

    public TogetherFoldersResponseDto getTogetherFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        ArrayList<FolderIdNameMapping> folderIdNameMappings = folderRepository.findByUserIdAndIsAlonedOrderByCreatedAtDesc(userId, false);

        return new TogetherFoldersResponseDto(folderIdNameMappings);
    }
}
