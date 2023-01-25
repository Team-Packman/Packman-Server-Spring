package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.list.ListTitleRequestDto;
import packman.service.ListService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class ListController {
    private final ListService listService;

    @PatchMapping("/title")
    public ResponseEntity<ResponseMessage> updateTitle(@RequestBody ListTitleRequestDto listTitleRequestDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.UPDATE_LIST_TITLE_SUCCESS,
                listService.updateTitle(listTitleRequestDto, userId)
        );
    }


}
