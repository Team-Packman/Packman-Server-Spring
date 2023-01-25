package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.FolderService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {
    private final
    FolderService folderService;

    @GetMapping("/list/alone/{folderId}")
    public ResponseEntity<ResponseMessage> getAloneListsInFolder(@PathVariable Long folderId, HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_LISTS_IN_FOLDER,
                folderService.getAloneListsInFolder(userId, folderId)
        );
    }
}
