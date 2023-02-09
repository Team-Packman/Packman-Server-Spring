package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import packman.dto.list.DepartureDateRequestDto;
import packman.dto.list.ListTitleRequestDto;
import packman.service.ListService;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class ListController {
    private final ListService listService;

    /*
    패킹리스트 제목 수정
     */
    @PatchMapping("/title")
    public ResponseEntity<ResponseMessage> updateTitle(@RequestBody @Valid ListTitleRequestDto listTitleRequestDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.UPDATE_LIST_TITLE_SUCCESS,
                listService.updateTitle(listTitleRequestDto, userId)
        );
    }

    /*
    패킹리스트 출발날짜 수정
     */
    @PatchMapping("/departureDate")
    public ResponseEntity<ResponseMessage> updateDepartureDate(@RequestBody @Valid DepartureDateRequestDto departureDateRequestDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.UPDATE_LIST_DEPARTURE_DATE_SUCCESS,
                listService.updateDepartureDate(departureDateRequestDto, userId)
        );
    }

    @GetMapping("/{listId}/title-date")
    public ResponseEntity<ResponseMessage> getPackingListTitleAndDate(@PathVariable Long listId, @RequestParam boolean isAloned, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.GET_LIST_TITLE_DEPARTURE_DATE_SUCCESS,
                listService.getPackingListTitleAndDate(listId, isAloned, userId)
        );
    }

}
