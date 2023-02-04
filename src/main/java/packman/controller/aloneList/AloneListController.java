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

    @DeleteMapping("/{folderId}/{listId}")
    public ResponseEntity<ResponseMessage> deleteAloneList(@PathVariable("folderId") Long folderId, @PathVariable("listId") List<Long> listIds,  HttpServletRequest request) {
        Long userId = 1L;

        aloneListService.deleteAloneList(userId, folderId, listIds);

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_ALONE_LIST
        );
    }

}
