package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PatchMapping("/title")
    public ResponseEntity<ResponseMessage> updateTitle(@RequestBody @Valid ListTitleRequestDto listTitleRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        Long userId = 1L;

        if(bindingResult.hasErrors()){
            throw new CustomException(ResponseCode.NULL_VALUE);
        }
        return ResponseMessage.toResponseEntity(
                ResponseCode.UPDATE_LIST_TITLE_SUCCESS,
                listService.updateTitle(listTitleRequestDto, userId)
        );
    }


}
