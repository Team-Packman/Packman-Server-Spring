package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.folder.FolderRequestDto;
import packman.dto.folder.FolderUpdateRequestDto;
import packman.service.FolderService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/alone")
    public ResponseEntity<ResponseMessage> getAloneFolders(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_FOLDERS,
                folderService.getAloneFolders(userId)
        );
    }

    @GetMapping("/together")
    public ResponseEntity<ResponseMessage> getTogetherFolders(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_FOLDERS,
                folderService.getTogetherFolders(userId)
        );
    }

    @GetMapping("/list/alone/{folderId}")
    public ResponseEntity<ResponseMessage> getAloneListsInFolder(@PathVariable Long folderId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_LISTS_IN_FOLDER,
                folderService.getAloneListsInFolder(userId, folderId)
        );
    }

    @GetMapping("/list/together/{folderId}")
    public ResponseEntity<ResponseMessage> getTogetherListsInFolder(@PathVariable Long folderId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_LISTS_IN_FOLDER,
                folderService.getTogetherListsInFolder(userId, folderId)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createFolder(@RequestBody @Valid FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_FOLDER,
                folderService.createFolder(folderRequestDto, userId)
        );
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getFolders(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_FOLDERS,
                folderService.getFolders(userId)
        );
    }

    @PatchMapping
    public ResponseEntity<ResponseMessage> updateFolder(@RequestBody @Valid FolderUpdateRequestDto folderUpdateRequestDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_FOLDER,
                folderService.updateFolder(folderUpdateRequestDto, userId)
        );
    }

    @GetMapping("/recentCreatedList")
    public ResponseEntity<ResponseMessage> getRecentCreatedList(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        if (folderService.getRecentCreatedList(userId) == null) {
            return ResponseMessage.toResponseEntity(
                    ResponseCode.NO_EXIST_USER_LIST,
                    Collections.emptyList()
            );
        } else {
            return ResponseMessage.toResponseEntity(
                    ResponseCode.GET_RECENT_CREATED_LIST_SUCCESS,
                    folderService.getRecentCreatedList(userId)
            );
        }
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<ResponseNonDataMessage> deleteFolder(@PathVariable("folderId") String folderId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        folderService.deleteFolder(Long.parseLong(folderId), userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_FOLDER
        );
    }

}
