package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.AloneFolderMapping;
import packman.dto.folder.AloneFoldersResponseDto;
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

        ArrayList<AloneFolderMapping> aloneFolderMappings = folderRepository.findByUserIdAndIsAlonedOrderByCreatedAtDesc(userId, true);

        return new AloneFoldersResponseDto(aloneFolderMappings);
    }
}
