package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.FolderService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.dto.folder.FolderRequestDto;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/alone")
    public ResponseEntity<ResponseMessage> getAloneFolders(HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_FOLDERS,
                folderService.getAloneFolders(userId)
        );
    }

    @GetMapping("/together")
    public ResponseEntity<ResponseMessage> getTogetherFolders(HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_FOLDERS,
                folderService.getTogetherFolders(userId)
        );
    }

    @GetMapping("/list/alone/{folderId}")
    public ResponseEntity<ResponseMessage> getAloneListsInFolder(@PathVariable Long folderId, HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_LISTS_IN_FOLDER,
                folderService.getAloneListsInFolder(userId, folderId)
        );
    }

    @GetMapping("/list/together/{folderId}")
    public ResponseEntity<ResponseMessage> getTogetherListsInFolder(@PathVariable Long folderId, HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_LISTS_IN_FOLDER,
                folderService.getTogetherListsInFolder(userId, folderId)
        );
    }
    @PostMapping
    public ResponseEntity<ResponseMessage> createFolder(@RequestBody @Valid FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_FOLDER,
                folderService.createFolder(folderRequestDto, userId)
        );
    }
}
