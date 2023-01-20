package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.UserService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getUser(HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_USER,
                userService.getUser(userId)
        );
    }

    @DeleteMapping
    public ResponseEntity<ResponseNonDataMessage> deleteUser(HttpServletRequest request) {
        Long userId = 1L;  //  임시 userId 1

        userService.deleteUser(userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_USER
        );
    }
}
