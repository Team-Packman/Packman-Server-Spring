package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.auth.JwtTokenProvider;
import packman.dto.auth.AuthKakaoRequestDto;
import packman.dto.auth.KakaoLoginNewUserResponseDto;
import packman.dto.auth.KakaoLoginResponseDto;
import packman.service.AuthService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/kakao")
    public ResponseEntity<ResponseMessage> kakaoLogin(@RequestBody @Valid AuthKakaoRequestDto authKakaoRequestDto) {

        KakaoLoginResponseDto kakaoUserProfile = authService.getKakaoUserProfile(authKakaoRequestDto);

        if (!kakaoUserProfile.isAlreadyUser()) {
            KakaoLoginNewUserResponseDto kakaoLoginNewUserResponseDto = KakaoLoginNewUserResponseDto.builder()
                    .alreadyUser(kakaoUserProfile.isAlreadyUser())
                    .email(kakaoUserProfile.getEmail())
                    .name(kakaoUserProfile.getName())
                    .gender(kakaoUserProfile.getGender())
                    .ageRange(kakaoUserProfile.getAgeRange())
                    .build();

            return ResponseMessage.toResponseEntity(
                    ResponseCode.SUCCESS_KAKAO_LOGIN,
                    kakaoLoginNewUserResponseDto
            );
        }

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_KAKAO_LOGIN,
                kakaoUserProfile
        );
    }

    @CrossOrigin(origins = {"https://www.packman.kr", "https://www.packgirl.ml", "http://localhost:3000"}, allowedHeaders = "*")
    @GetMapping("/token")
    public ResponseEntity<ResponseMessage> getNewToken(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_NEW_TOKEN,
                authService.getNewToken(accessToken, refreshToken));
    }
}
