package packman.service;

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

import java.util.Optional;

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

        // 신규 유저
        if (optionalUser.isEmpty()) {
            return kakaoLoginResponseDto;
        }

        // 탈퇴한 유저
        User user = optionalUser.get();
        if (user.isDeleted()) {
            userRepository.delete(user);
            return kakaoLoginResponseDto;
        }

        user.setRefreshToken(jwtTokenProvider.createRefreshToken());

        // 선택사항 동의 유무 확인하는 코드 추가!

        // 존재하는 유저
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

    // 카카오 accessToken으로 유저 정보 받아오기
    public KakaoLoginResponseDto getKakaoUserProfile(AuthKakaoRequestDto authKakaoRequestDto) {
        // 서버에서 테스트할 때
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

        KakaoUserProfileDto kakaoUserProfileDto = KakaoUserProfileDto.builder()
                .name(kakaoProfileDto.properties.nickname)
                .email(kakaoProfileDto.kakao_account.email)
                .ageRange(kakaoProfileDto.kakao_account.age_range)
                .gender(kakaoProfileDto.kakao_account.gender)
                .build();

        return kakaoLogin(kakaoUserProfileDto);
    }

    // 서버에서 code로 accessToken 생성할 때 사용
    public String getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);

        // HttpHeader와 HttpsBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기
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
}
