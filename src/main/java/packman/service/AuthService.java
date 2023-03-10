package packman.service;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import packman.auth.JwtTokenProvider;
import packman.dto.auth.*;
import packman.entity.User;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import java.util.Optional;

import static packman.validator.Validator.validateUserRefreshToken;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.authUrl}")
    private String kakaoAuthUrl;

    @Value("${kakao.userProfileUrl}")
    private String kakaoUserProfileUrl;

    public KakaoLoginResponseDto kakaoLogin(KakaoUserProfileDto kakaoUserProfileDto) {
        String email = kakaoUserProfileDto.getEmail();

        KakaoLoginResponseDto kakaoLoginResponseDto = KakaoLoginResponseDto.builder()
                .alreadyUser(false)
                .email(kakaoUserProfileDto.getEmail())
                .name(kakaoUserProfileDto.getName())
                .gender(kakaoUserProfileDto.getGender())
                .ageRange(kakaoUserProfileDto.getAgeRange())
                .build();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        // ?????? ??????
        if (optionalUser.isEmpty()) {
            return kakaoLoginResponseDto;
        }

        // ????????? ??????
        User user = optionalUser.get();
        if (user.isDeleted()) {
            userRepository.delete(user);
            return kakaoLoginResponseDto;
        }

        user.setRefreshToken(jwtTokenProvider.createRefreshToken());

        // ???????????? ?????? ?????? ???????????? ?????? ??????!

        // ???????????? ??????
        return KakaoLoginResponseDto.builder()
                .alreadyUser(true)
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .accessToken(jwtTokenProvider.createAccessToken(user.getId().toString()))
                .refreshToken(user.getRefreshToken())
                .build();
    }

    // ????????? accessToken?????? ?????? ?????? ????????????
    public KakaoLoginResponseDto getKakaoUserProfile(AuthKakaoRequestDto authKakaoRequestDto) {
        // ???????????? ???????????? ???
//        String accessToken = getKakaoAccessToken(authKakaoRequestDto.getAccessToken());

        String accessToken = authKakaoRequestDto.getAccessToken();
        
        RestTemplate rt2 = new RestTemplate();
        
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + accessToken);
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        
        ResponseEntity<String> response2 = rt2.exchange(
                kakaoUserProfileUrl,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfileDto kakaoProfileDto;
        try {
            kakaoProfileDto = objectMapper2.readValue(response2.getBody(), KakaoProfileDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // true??? ?????? ?????? x
        String ageRange = kakaoProfileDto.kakao_account.age_range_needs_agreement ? "" : kakaoProfileDto.kakao_account.age_range;
        String gender = kakaoProfileDto.kakao_account.gender_needs_agreement ? "" : kakaoProfileDto.kakao_account.gender;

        KakaoUserProfileDto kakaoUserProfileDto = KakaoUserProfileDto.builder()
                .name(kakaoProfileDto.properties.nickname)
                .email(kakaoProfileDto.kakao_account.email)
                .ageRange(ageRange)
                .gender(gender)
                .build();

        return kakaoLogin(kakaoUserProfileDto);
    }

    // ???????????? code??? accessToken ????????? ??? ??????
    public String getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader ???????????? ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody ???????????? ??????
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);

        // HttpHeader??? HttpsBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http ????????????
        ResponseEntity<String> response = rt.exchange(
                kakaoAuthUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenDto kakaoTokenDto;
        try {
            kakaoTokenDto = objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoTokenDto.getAccess_token();
    }

    public NewTokenResponseDto getNewToken(String accessToken, String refreshToken) {
        Long userId = Long.valueOf(JWT.decode(accessToken).getSubject());

        validateUserRefreshToken(userRepository, userId, refreshToken);

        String validateAccessToken = jwtTokenProvider.isValidateToken(accessToken);
        String validateRefreshToken = jwtTokenProvider.isValidateToken(refreshToken);

        if (validateRefreshToken.equals("expired_token")) {
            throw new CustomException(ResponseCode.REFRESH_TOKEN_EXPIRED);
        }

        if (validateAccessToken.equals("expired_token")) {
            String newAccessToken = jwtTokenProvider.createAccessToken(userId.toString());

            return NewTokenResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        // ??? ??? ???????????? ??????
        throw new CustomException(ResponseCode.VALID_ACCESS_TOKEN);
    }
}
