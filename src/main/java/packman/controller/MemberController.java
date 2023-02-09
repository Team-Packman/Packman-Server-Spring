package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import packman.service.MemberService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{listId}")
    public ResponseEntity<ResponseMessage> getMember(@PathVariable @NotBlank String listId, HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_MEMBER,
                memberService.getMember(userId, Long.valueOf(listId))
        );
    }

    @DeleteMapping("/{groupId}/{memberId}")
    public ResponseEntity<ResponseNonDataMessage> deleteMember(@PathVariable Long groupId, @PathVariable List<Long> memberId, HttpServletRequest request) {
        Long userId = 1L;  // 임시 userId 1

        memberService.deleteMember(userId, groupId, memberId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_MEMBER
        );
    }
}
