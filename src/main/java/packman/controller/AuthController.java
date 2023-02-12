package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.auth.JwtTokenProvider;
import packman.dto.auth.AuthKakaoLoginNewResponseDto;
import packman.dto.auth.AuthKakaoLoginResponseDto;
import packman.dto.auth.AuthKakaoTokenDto;
import packman.service.AuthService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/kakao")
    public ResponseEntity<ResponseMessage> kakaoLogin(@RequestBody @Valid AuthKakaoTokenDto authKakaoTokenDto) {

        AuthKakaoLoginResponseDto kakaoUserProfile = authService.getKakaoUserProfile(authKakaoTokenDto);

        if (!kakaoUserProfile.isAlreadyUser()) {
            AuthKakaoLoginNewResponseDto authKakaoLoginNewResponseDto = AuthKakaoLoginNewResponseDto.builder()
                    .alreadyUser(kakaoUserProfile.isAlreadyUser())
                    .email(kakaoUserProfile.getEmail())
                    .name(kakaoUserProfile.getName())
                    .gender(kakaoUserProfile.getGender())
                    .ageRange(kakaoUserProfile.getAgeRange())
                    .build();

            return ResponseMessage.toResponseEntity(
                    ResponseCode.SUCCESS_KAKAO_LOGIN,
                    authKakaoLoginNewResponseDto
            );
        }

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_KAKAO_LOGIN,
                kakaoUserProfile
        );
    }
}
