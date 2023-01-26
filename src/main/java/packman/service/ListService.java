package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.DepartureDateRequestDto;
import packman.dto.list.DepartureDateResponseDto;
import packman.dto.list.ListTitleRequestDto;
import packman.dto.list.ListTitleResponseDto;
import packman.repository.UserRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class ListService {
    private final UserRepository userRepository;
    private final PackingListRepository listRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;

    public ListTitleResponseDto updateTitle(ListTitleRequestDto listTitleRequestDto, Long userId) {
        Long listId = Long.parseLong(listTitleRequestDto.getId());
        String title = listTitleRequestDto.getTitle();

        //유저 검증
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        //제목 글자수 검증
        if (title.length() > 12) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }

        if (!listTitleRequestDto.getIsAloned()) {
            listId = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                    () -> new CustomException(ResponseCode.NO_LIST))
                    .getTogetherPackingList().getPackingList().getId();
        }

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setTitle(title);
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

        return new ListTitleResponseDto(listTitleRequestDto.getId(), title);
    }

    public DepartureDateResponseDto updateDepartureDate(DepartureDateRequestDto departureDateRequestDto, Long userId) {
        Long listId = Long.parseLong(departureDateRequestDto.getId());
        LocalDate departureDate = LocalDate.parse(departureDateRequestDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);

        //유저 검증
        userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        if (!departureDateRequestDto.getIsAloned()) {
            listId = togetherAlonePackingListRepository.findById(listId).orElseThrow(
                            () -> new CustomException(ResponseCode.NO_LIST))
                    .getTogetherPackingList().getPackingList().getId();
        }

        listRepository.findByIdAndIsDeleted(listId, false).ifPresentOrElse(t -> {
            t.setDepartureDate(departureDate);
        }, () -> {throw new CustomException(ResponseCode.NO_LIST);});

        return new DepartureDateResponseDto(departureDateRequestDto.getId(), departureDateRequestDto.getDepartureDate());
    }
}
