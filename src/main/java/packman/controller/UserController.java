package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.user.UserRequestDto;
import packman.service.UserService;
import packman.util.CustomException;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return ResponseMessage.toResponseEntity(
                ResponseCode.CREATE_USER_SUCCESS,
                userService.createUser(userRequestDto)
        );
    }

}
