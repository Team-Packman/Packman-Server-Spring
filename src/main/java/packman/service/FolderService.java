package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.AloneFolderMapping;
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

    public ArrayList<AloneFolderMapping> getAloneFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        return folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
    }
}
