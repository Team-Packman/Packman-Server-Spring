package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.member.MemberAddDto;
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

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<ResponseMessage> getInviteTogetherList(@PathVariable("inviteCode") String inviteCode, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_INVITE_TOGETHER_PACKING,
                togetherListService.getInviteTogetherList(userId, inviteCode)
        );
    }

    @PostMapping("/add-member")
    public ResponseEntity<ResponseMessage> addMember(@RequestBody @Valid MemberAddDto
                                                             memberAddDto, HttpServletRequest httpServletRequest) {
        Long userId = 5L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_ADD_MEMBER,
                togetherListService.addMember(memberAddDto, userId)
        );
    }
}
