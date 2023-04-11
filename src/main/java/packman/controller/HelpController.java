package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.HelpService;
import packman.util.ResponseCode;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/help")
public class HelpController {
    private final HelpService helpService;

    @GetMapping
    public ResponseEntity<ResponseNonDataMessage> getAloneFolders(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        helpService.getHelp(userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_HELP
        );
    }
}
