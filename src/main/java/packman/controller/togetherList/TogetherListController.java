package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.list.ListDto;
import packman.service.togetherList.TogetherListService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/together")
public class TogetherListController {
    private final TogetherListService togetherListService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createTogetherList(@RequestBody @Valid ListDto listDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_TOGETHER_LIST,
                togetherListService.createTogetherList(listDto, userId)
        );
    }
}
