package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.FolderMapping;
import packman.dto.folder.FolderRequestDto;
import packman.dto.folder.FolderResponseDto;
import packman.entity.Folder;
import packman.entity.User;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderResponseDto createFolder(FolderRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        String name = request.getName();

        // validation
        if (name.length() > 8) {
            throw new CustomException(ResponseCode.CREATE_FOLDER_FAIL);
        }

        Folder folder = new Folder(request, user);
        folderRepository.save(folder);

        ArrayList<FolderMapping> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, true);
        ArrayList<FolderMapping> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, false);

        return new FolderResponseDto(
                aloneFolders,
                togetherFolders
        );
    }

}
