package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.list.ListCreateDto;
import packman.service.aloneList.AloneListService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone")
public class AloneListController {
    private final AloneListService aloneListService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createAloneList(@RequestBody @Valid ListCreateDto listCreateDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_LIST,
                aloneListService.createAloneList(listCreateDto, userId)
        );
    }

    @GetMapping("/{listId}")
    public ResponseEntity<ResponseMessage> createAloneList(@PathVariable("listId") Long listId, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_LIST,
                aloneListService.getAloneList(listId, userId)
        );
    }

}
