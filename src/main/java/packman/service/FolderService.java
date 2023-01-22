package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.folder.FolderMapping;
import packman.dto.folder.FolderRequestDto;
import packman.dto.folder.FolderResponseDto;
import packman.entity.Folder;
import packman.entity.FolderPackingList;
import packman.entity.User;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final PackingListRepository packingListRepository;

    public FolderResponseDto createFolder(FolderRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        String name = request.getName();

        // validation
        if (name.length() > 8) {
            throw new CustomException(ResponseCode.FAIL_CREATE_FOLDER);
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

    public FolderResponseDto getFolders(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        ArrayList<FolderMapping> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, true);
        ArrayList<FolderMapping> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, false);

        return new FolderResponseDto(
                aloneFolders,
                togetherFolders
        );
    }

    public FolderResponseDto deleteFolder(Long folderId, Long userId) {
        Folder folder = folderRepository
                .findByIdAndUserId(folderId, userId)
                .orElseThrow(
                        () -> new CustomException(ResponseCode.NO_FOLDER)
                );
        List<FolderPackingList> folderPackingLists = folder.getFolderPackingList();
        if (folder.getIsAloned()) {
//            AlonePackingListService의 delete 호출

//            Stream<AlonePackingList> alonePackingLists = folderPackingLists.stream().map(folderPackingList -> folderPackingList.getAlonePackingList());
//            List<PackingList> packingList = alonePackingLists.map(alonePackingList -> alonePackingList.getPackingList())
//                    .collect(Collectors.toList());;
//            packingList.forEach( list -> list.setIdDeleted(true));
//            packingListRepository.saveAll(packingList);
        } else {
            List<TogetherAlonePackingList> togetherAlonePackingList = folderPackingLists.stream()
                    .map( p -> {
                return togetherAlonePackingListRepository.findByAlonePackingList(p.getAlonePackingList());
                        })
                    .collect(Collectors.toList());

            // 채워야할 곳
            // togetherAlonePackingList에서 받은 id로 togetherListService.deleteTogetherList() 호출
            //
        }
        // 폴더 삭제
        folderRepository.deleteById(folderId);

        ArrayList<FolderMapping> aloneFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, true);
        ArrayList<FolderMapping> togetherFolders = folderRepository.findByUserIdAndIsAlonedOrderByUserIdDesc(userId, false);

        return new FolderResponseDto(
                aloneFolders,
                togetherFolders
        );
    }

}
