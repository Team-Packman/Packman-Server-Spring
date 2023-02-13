package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.list.ListCreateDto;
import packman.dto.member.MemberAddDto;
import packman.dto.togetherList.PackerUpdateDto;
import packman.service.togetherList.TogetherListService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/list/together")
public class TogetherListController {
    private final TogetherListService togetherListService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createTogetherList(@RequestBody @Valid ListCreateDto listCreateDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_TOGETHER_LIST,
                togetherListService.createTogetherList(listCreateDto, userId)
        );
    }

    @DeleteMapping("/{folderId}/{listId}")
    public ResponseEntity<ResponseNonDataMessage> deleteTogetherList(@PathVariable("folderId") Long folderId, @PathVariable("listId") List<Long> listIds, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        togetherListService.deleteTogetherList(userId, folderId, listIds);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_TOGETHER_LIST
        );
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<ResponseMessage> getInviteTogetherList(@PathVariable("inviteCode") String inviteCode, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_INVITE_TOGETHER_PACKING,
                togetherListService.getInviteTogetherList(userId, inviteCode)
        );
    }

    @GetMapping("/{listId}")
    public ResponseEntity<ResponseMessage> getTogetherList(@PathVariable("listId") Long listId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_LIST,
                togetherListService.getTogetherList(listId, userId)
        );
    }

    @PatchMapping("/packer")
    public ResponseEntity<ResponseMessage> updatePacker(@RequestBody @Valid PackerUpdateDto packerUpdateDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_PACKER,
                togetherListService.updatePacker(packerUpdateDto, userId)
        );
    }

    @PostMapping("/add-member")
    public ResponseEntity<ResponseMessage> addMember(@RequestBody @Valid MemberAddDto memberAddDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_ADD_MEMBER,
                togetherListService.addMember(memberAddDto, userId)
        );
    }
}
