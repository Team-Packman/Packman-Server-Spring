package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.list.ListCreateDto;
import packman.service.aloneList.AloneListService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<ResponseMessage> getAloneList(@PathVariable("listId") Long listId, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_LIST,
                aloneListService.getAloneList(listId, userId)
        );
    }

    @DeleteMapping("/{folderId}/{listId}")
    public ResponseEntity<ResponseNonDataMessage> deleteAloneList(@PathVariable("folderId") Long folderId, @PathVariable("listId") List<Long> listIds, HttpServletRequest request) {
        Long userId = 1L;

        aloneListService.deleteAloneList(userId, folderId, listIds);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_ALONE_LIST
        );
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<ResponseMessage> getInviteAloneList(@PathVariable String inviteCode, HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_INVITE_ALONE_LIST,
                aloneListService.getInviteAloneList(userId, inviteCode)
        );
    }
}
