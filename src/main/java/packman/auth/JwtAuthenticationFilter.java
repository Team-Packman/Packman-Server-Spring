package packman.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import packman.entity.User;
import packman.util.ResponseCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);  // header에서 가져온 accessToken

        if (request.getRequestURI().equals("/auth/token")) {
            String refreshToken = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);

            // 만료된 refreshToken
            if (jwtTokenProvider.isValidateToken(refreshToken).equals("ok")) {
                if (jwtTokenProvider.isValidateToken(accessToken).equals("ok")) {
                    setErrorResponse((HttpServletResponse) response, ResponseCode.VALID_ACCESS_TOKEN);
                    logger.error(ResponseCode.VALID_ACCESS_TOKEN.getMessage());
                    return;
                } else if (accessToken.equals("")) {
                    setErrorResponse((HttpServletResponse) response, ResponseCode.NO_TOKEN);
                    logger.error(ResponseCode.NO_TOKEN.getMessage());
                    return;
                } else if (jwtTokenProvider.isValidateToken(accessToken).equals("invalid_token")) {
                    setErrorResponse((HttpServletResponse) response, ResponseCode.INVALIDE_TOKEN);
                    logger.error(ResponseCode.INVALIDE_TOKEN.getMessage());
                    return;
                } else if (jwtTokenProvider.isValidateToken(accessToken).equals("expired_token")) {
                    // 토큰 재발급 성공
                    Long userId = jwtTokenProvider.validateUserRefreshToken(accessToken, refreshToken);

                    String newAccessToken = jwtTokenProvider.createAccessToken(userId.toString());

                    setAuthentication(newAccessToken);
                    request.setAttribute("newAccessToken", newAccessToken);
                } else {
                    logger.error("토큰 관련 예외 exception");
                    return;
                }
            } else if (jwtTokenProvider.isValidateToken(refreshToken).equals("expired_token")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.REFRESH_TOKEN_EXPIRED);
                logger.error(ResponseCode.REFRESH_TOKEN_EXPIRED.getMessage());
                return;
            } else if (refreshToken.equals("")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.NO_TOKEN);
                logger.error(ResponseCode.NO_TOKEN.getMessage());
                return;
            } else if (jwtTokenProvider.isValidateToken(refreshToken).equals("invalid_token")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.INVALIDE_TOKEN);
                logger.error(ResponseCode.INVALIDE_TOKEN.getMessage());
                return;
            } else {
                logger.error("토큰 관련 예외 exception");
                return;
            }
        } else {
            if (jwtTokenProvider.isValidateToken(accessToken).equals("ok")) {  // token 검증
                String userId = setAuthentication(accessToken);

                if (userId == null) {
                    setErrorResponse(response, ResponseCode.NO_USER);
                    logger.error(ResponseCode.NO_USER.getMessage());
                    return;
                }
            } else if (accessToken.equals("")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.NO_TOKEN);
                logger.error(ResponseCode.NO_TOKEN.getMessage());
                return;
            } else if (jwtTokenProvider.isValidateToken(accessToken).equals("invalid_token")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.INVALIDE_TOKEN);
                logger.error(ResponseCode.INVALIDE_TOKEN.getMessage());
                return;
            } else if (jwtTokenProvider.isValidateToken(accessToken).equals("expired_token")) {
                setErrorResponse((HttpServletResponse) response, ResponseCode.EXPIRED_TOKEN);
                logger.error(ResponseCode.EXPIRED_TOKEN.getMessage());
                return;
            } else {
                logger.error("토큰 관련 예외 exception");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    // token으로 userId 얻기
    private String setAuthentication(String jwtToken) {
        try {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);  // 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);  // SecurityContextHolder에 인증 객체 저장
            User user = (User) authentication.getPrincipal();
            return user.getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ResponseCode responseCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(responseCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(response.getStatus(), false, responseCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final Integer status;
        private final Boolean success;
        private final String message;
    }
}