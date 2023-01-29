package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.list.ListRequestDto;
import packman.service.aloneList.AloneListService;
import packman.util.CustomException;
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
    public ResponseEntity<ResponseMessage> createAloneList(@RequestBody @Valid ListRequestDto listRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        Long userId = 1L;

        if(bindingResult.hasErrors()){
            throw new CustomException(ResponseCode.NULL_VALUE);
        }

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_LIST,
                aloneListService.createAloneList(listRequestDto, userId)
        );
    }

}
