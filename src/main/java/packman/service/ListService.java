package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ListService {
    private final UserRepository userRepository;
    private final PackingListRepository listRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final PackingListRepository packingListRepository;

    public ListTitleResponseDto updateTitle(ListTitleRequestDto listTitleRequestDto, Long userId) {
        Long listId = Long.parseLong(listTitleRequestDto.getId());
        Long aloneListId = listId;
        String title = listTitleRequestDto.getTitle();

        //유저 검증
        validateUserId(userRepository, userId);

        //제목 글자수 검증
        validateListLength(title);

        if (!listTitleRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_LIST));
            listId = togetherAlonePackingList.getTogetherPackingList().getId();
            aloneListId = togetherAlonePackingList.getAlonePackingList().getId();
        }

        // 유저의 패킹리스트인지 검증
        validateUserList(folderPackingListRepository, userId, aloneListId);

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setTitle(title);
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

        return new ListTitleResponseDto(listTitleRequestDto.getId(), title);
    }

    public DepartureDateResponseDto updateDepartureDate(DepartureDateRequestDto departureDateRequestDto, Long userId) {
        Long listId = Long.parseLong(departureDateRequestDto.getId());
        Long aloneListId = listId;
        LocalDate departureDate = LocalDate.parse(departureDateRequestDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);

        //유저 검증
        validateUserId(userRepository, userId);

        if (!departureDateRequestDto.getIsAloned()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_LIST));
            listId = togetherAlonePackingList.getTogetherPackingList().getId();
            aloneListId = togetherAlonePackingList.getAlonePackingList().getId();
        }

        // 유저의 패킹리스트인지 검증
        validateUserList(folderPackingListRepository, userId, aloneListId);

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setDepartureDate(departureDate);
        }, () -> {
            throw new CustomException(ResponseCode.NO_LIST);
        });

        return new DepartureDateResponseDto(departureDateRequestDto.getId(), departureDateRequestDto.getDepartureDate());
    }

    public InviteListResponseDto getInviteList(String listType, String inviteCode) {
        Long listId;
        String title, departureDate;

        if (listType.equals("alone")) {
            AlonePackingList alonePackingList = validateAlonePackingListByInviteCode(alonePackingListRepository, inviteCode);
            listId = alonePackingList.getId();
            title = alonePackingList.getPackingList().getTitle();
            departureDate = alonePackingList.getPackingList().getDepartureDate().toString();
        } else if (listType.equals("together")) {
            TogetherPackingList togetherPackingList = validateTogetherPackingListByInviteCode(togetherPackingListRepository, inviteCode);
            listId = togetherPackingList.getId();
            title = togetherPackingList.getPackingList().getTitle();
            departureDate = togetherPackingList.getPackingList().getDepartureDate().toString();
        } else {
            throw new CustomException(ResponseCode.INVALID_LIST_TYPE);
        }

        ListResponseMapping listResponseMapping = packingListRepository.findByIdAndTitle(listId, title);

        return InviteListResponseDto.builder()
                .id(listResponseMapping.getId())
                .title(title)
                .departureDate(departureDate)
                .category(listResponseMapping.getCategory())
                .build();
    }
}
