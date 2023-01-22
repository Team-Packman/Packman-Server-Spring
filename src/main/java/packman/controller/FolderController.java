package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.folder.FolderRequestDto;
import packman.service.FolderService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createFolder(@RequestBody @Valid FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_FOLDER,
                folderService.createFolder(folderRequestDto, userId)
        );
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getFolders(HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_FOLDERS,
                folderService.getFolders(userId)
        );
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<ResponseMessage> deleteFolder(@PathVariable("folderId") String folderId, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_FOLDER,
                folderService.deleteFolder(Long.parseLong(folderId), userId)
        );
    }

}
