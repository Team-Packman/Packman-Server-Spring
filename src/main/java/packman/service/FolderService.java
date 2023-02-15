package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryPackMapping;
import packman.dto.folder.*;
import packman.dto.list.ListInFolderDto;
import packman.dto.list.RecentCreatedListResponseDto;
import packman.dto.list.TogetherAloneListMapping;
import packman.dto.pack.PackCountMapping;
import packman.entity.Folder;
import packman.entity.FolderPackingList;
import packman.entity.User;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.repository.CategoryRepository;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.service.togetherList.TogetherListService;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.service.aloneList.AloneListService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static packman.validator.IdValidator.validateUserId;
import static packman.validator.Validator.validateFolder;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final PackingListRepository packingListRepository;
    private final CategoryRepository categoryRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final AloneListService aloneListService;
    private final TogetherListService togetherListService;

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

    public FolderResponseDto createFolder(FolderRequestDto request, Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        String name = request.getName();

        // validation
        if (name.length() > 8) {
            throw new CustomException(ResponseCode.FAIL_CREATE_FOLDER);
        }

        Folder folder = new Folder(user, name, request.getIsAloned());
        folderRepository.save(folder);

        List<Folder> folders = folderRepository.findByUserIdOrderByIdDesc(userId);

        List<FolderInfo> aloneFoldersResponse = getResponseFoldersByIsAloned(folders, true);

        List<FolderInfo> togetherFoldersResponse = getResponseFoldersByIsAloned(folders, false);

        return new FolderResponseDto(
                aloneFoldersResponse,
                togetherFoldersResponse
        );
    }

    public FolderResponseDto getFolders(Long userId) {
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        List<Folder> folders = folderRepository.findByUserIdOrderByIdDesc(userId);

        List<FolderInfo> aloneFoldersResponse = getResponseFoldersByIsAloned(folders, true);

        List<FolderInfo> togetherFoldersResponse = getResponseFoldersByIsAloned(folders, false);

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

        List<Folder> folders = folderRepository.findByUserIdOrderByIdDesc(userId);

        List<FolderInfo> aloneFoldersResponse = getResponseFoldersByIsAloned(folders, true);

        List<FolderInfo> togetherFoldersResponse = getResponseFoldersByIsAloned(folders, false);

        return new FolderResponseDto(
                aloneFoldersResponse,
                togetherFoldersResponse
        );

    }
    private List<FolderInfo> getResponseFoldersByIsAloned(List<Folder> folders, boolean isAloned) {
        List<Folder> foldersByIsAloned = folders.stream().filter(f -> f.getIsAloned() == isAloned)
                .collect(Collectors.toList());

        return foldersByIsAloned.stream().map(f -> {
            Long folderId = f.getId();
            String folderName = f.getName();
            String folderListNum = f.getListNum();
            return new FolderInfo(Long.toString(folderId), folderName, folderListNum);
        }).collect(Collectors.toList());
    }

    public RecentCreatedListResponseDto getRecentCreatedList(Long userId) {
        validateUserId(userRepository, userId);

        List<AlonePackingList> alonePackingLists = alonePackingListRepository.findByFolderPackingList_Folder_UserIdOrderByIdDesc(userId);

        if (alonePackingLists.size() == 0) {
            return null;
        }

        AlonePackingList alonePackingList = alonePackingLists.get(0);
        Long recentListId = alonePackingList.getId();
        List<ListInFolderDto> listInFolderDtos = new ArrayList<>();

        Long togetherId;
        String url;

        // 나의 패킹리스트인 경우
        if (!alonePackingList.isAloned()) {
            TogetherAloneListMapping togetherAloneListMapping = togetherAlonePackingListRepository.findByAlonePackingListId(recentListId);
            recentListId = togetherAloneListMapping.getId();
            togetherId = togetherAloneListMapping.getTogetherPackingList().getId();

            addListInFolderDto(listInFolderDtos, recentListId, togetherId);
            url = "together?id=" + recentListId;
        } else {
            addListInFolderDto(listInFolderDtos, recentListId, recentListId);
            url = "alone?id=" + recentListId;
        }

        ListInFolderDto listInFolderDto = listInFolderDtos.get(0);

        String departureDate = listInFolderDto.getDepartureDate();
        LocalDate nowDate = LocalDate.now();

        long remainDay = ChronoUnit.DAYS.between(nowDate, LocalDate.parse(departureDate, DateTimeFormatter.ISO_DATE));

        return RecentCreatedListResponseDto.builder()
                .id(recentListId.toString())
                .title(listInFolderDto.getTitle())
                .remainDay(String.valueOf(remainDay))
                .packTotalNum(listInFolderDto.getPackTotalNum())
                .packRemainNum(listInFolderDto.getPackRemainNum())
                .url(url).build();
    }

    public void deleteFolder(Long folderId, Long userId) {
        Folder folder =validateFolder(folderRepository, folderId);

        List<FolderPackingList> folderPackingLists = folder.getFolderPackingList();
        // 폴더 내 패킹리스트 삭제
        if (folder.getIsAloned()) {
            List<Long> aloneListIds = folderPackingLists.stream()
                    .map( folderPackingList -> folderPackingList.getAlonePackingList().getId())
                    .collect(Collectors.toList());

            aloneListIds.forEach(System.out::println);

            aloneListService.deleteAloneList(userId, folderId, aloneListIds);
        } else {
            List<Long> togetherAloneListIds = folderPackingLists.stream()
                    .map( folderPackingList -> { return togetherAlonePackingListRepository.findByAlonePackingList(folderPackingList.getAlonePackingList()).getId();})
                    .collect(Collectors.toList());

            togetherAloneListIds.forEach(System.out::println);

            togetherListService.deleteTogetherList(userId, folderId, togetherAloneListIds);
        }

        // 폴더 삭제
        folderRepository.deleteById(folderId);

    }
}
