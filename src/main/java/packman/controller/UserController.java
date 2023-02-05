package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.user.UserUpdateDto;
import packman.service.UserService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @PatchMapping("/profile")
    public ResponseEntity<ResponseMessage> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_USER,
                userService.updateUser(userUpdateDto, userId)
        );
    }
}
