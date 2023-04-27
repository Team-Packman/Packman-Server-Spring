package packman.auth;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final String secretKey;
    private final long accessTokenExpireLength;
    private final long refreshTokenExpireLength;


    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_AUTHORIZATION_HEADER = "Refresh";

    public JwtTokenProvider(
            UserDetailsService userDetailsService,
            UserRepository userRepository, @Value("${jwt.token.secret-key}") String secretKey,
            @Value("${jwt.access-token.expire-length}") long accessTokenExpireLength,
            @Value("${jwt.refresh-token.expire-length}") long refreshTokenExpireLength) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.secretKey = secretKey;
        this.accessTokenExpireLength = accessTokenExpireLength;
        this.refreshTokenExpireLength = refreshTokenExpireLength;
    }

    // JWT 토큰 생성
    public String createAccessToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpireLength);

        return Jwts.builder()
                .setClaims(claims)  // 데이터
                .setIssuedAt(now)  // 토큰 발행 일자
                .setExpiration(expiration)  // 만료 일자
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // secret 값, 암호화 알고리즘
                .compact();  // Token 생성
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpireLength);

        return Jwts.builder()
                .setIssuedAt(now)  // 토큰 발행 일자
                .setExpiration(expiration)  // 만료 일자
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // secret 값, 암호화 알고리즘
                .compact();  // Token 생성
    }

    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccessTokenPayload(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(ResponseCode.NO_USER.getMessage());
        }
    }

    // 토큰에서 회원 정보 추출
    public String getAccessTokenPayload(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token값을 가져옴
    public String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            return "";
        } else {
            return header.split(" ")[1];
        }

    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String header = request.getHeader(REFRESH_AUTHORIZATION_HEADER);

        return Objects.requireNonNullElse(header, "");
    }


    // 토큰의 유효성, 만료 일자 확인
    public String isValidateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return "ok";
        } catch (ExpiredJwtException ex) {
            return "expired_token";
        } catch (Exception ex) {
            return "invalid_token";
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long validateUserRefreshToken(String accessToken, String refreshToken) {
        Long userId = Long.valueOf(JWT.decode(accessToken).getSubject());

        userRepository.findByIdAndRefreshToken(userId, refreshToken).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER_REFRESH_TOKEN)
        );

        return userId;
    }
}
