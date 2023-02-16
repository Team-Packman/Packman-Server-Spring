package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.user.UserCreateDto;
import packman.service.UserService;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import org.springframework.web.bind.annotation.*;
import packman.dto.user.UserUpdateDto;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody @Valid UserCreateDto userCreateDto, HttpServletRequest request) {

        return ResponseMessage.toResponseEntity(
                ResponseCode.CREATE_USER_SUCCESS,
                userService.createUser(userCreateDto)
        );
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getUser(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_USER,
                userService.getUser(userId)
        );
    }

    @DeleteMapping
    public ResponseEntity<ResponseNonDataMessage> deleteUser(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        userService.deleteUser(userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_USER
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<ResponseMessage> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_USER,
                userService.updateUser(userUpdateDto, userId)
        );
    }
}
