package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.FolderInfo;
import packman.dto.folder.FolderRequestDto;
import packman.dto.folder.FolderResponseDto;
import packman.dto.folder.FolderUpdateRequestDto;
import packman.entity.Folder;
import packman.entity.User;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderResponseDto createFolder(FolderRequestDto request, Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        String name = request.getName();

        // validation
        if (name.length() > 8) {
            throw new CustomException(ResponseCode.FAIL_CREATE_FOLDER);
        }

        Folder folder = new Folder(request, user);
        folderRepository.save(folder);

        ArrayList<Folder> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
        ArrayList<Folder> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, false);

        List<FolderInfo> aloneFoldersResponse = aloneFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        List<FolderInfo> togetherFoldersResponse = togetherFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        return new FolderResponseDto(
                aloneFoldersResponse,
                togetherFoldersResponse
        );
    }

    public FolderResponseDto getFolders(Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        ArrayList<Folder> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
        ArrayList<Folder> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, false);

        List<FolderInfo> aloneFoldersResponse = aloneFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        List<FolderInfo> togetherFoldersResponse = togetherFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        return new FolderResponseDto(
                aloneFoldersResponse,
                togetherFoldersResponse
        );
    }

    public FolderResponseDto updateFolder(FolderUpdateRequestDto folderUpdateRequestDto, Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        String name = folderUpdateRequestDto.getName();

        // validation
        if (name.length() > 8) {
            throw new CustomException(ResponseCode.FAIL_CREATE_FOLDER);
        }
        Folder folder = folderRepository.findById(Long.parseLong(folderUpdateRequestDto.getId()))
                .orElseThrow(
                        () -> new CustomException(ResponseCode.NO_FOLDER)
                );

        folder.setName(folderUpdateRequestDto.getName());
        folderRepository.save(folder);

        ArrayList<Folder> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, true);
        ArrayList<Folder> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByIdDesc(userId, false);

        List<FolderInfo> aloneFoldersResponse = aloneFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        List<FolderInfo> togetherFoldersResponse = togetherFolders.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());

        return new FolderResponseDto(
                aloneFoldersResponse,
                togetherFoldersResponse
        );

    }

}
