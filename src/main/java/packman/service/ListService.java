package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.DepartureDateRequestDto;
import packman.dto.list.DepartureDateResponseDto;
import packman.dto.list.ListTitleRequestDto;
import packman.dto.list.ListTitleResponseDto;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.repository.FolderPackingListRepository;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.validateUserList;


@Service
@Transactional
@RequiredArgsConstructor
public class ListService {
    private final UserRepository userRepository;
    private final PackingListRepository listRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;

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
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

        return new DepartureDateResponseDto(departureDateRequestDto.getId(), departureDateRequestDto.getDepartureDate());
    }
}
