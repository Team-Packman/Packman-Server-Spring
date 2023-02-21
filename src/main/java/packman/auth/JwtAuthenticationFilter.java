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

        if (jwtTokenProvider.isValidateToken(accessToken).equals("ok")) {  // token 검증
            String userId = setAuthentication(accessToken);

            if (userId == null) {
                setErrorResponse(response, ResponseCode.NO_USER);
                logger.info("userId is null");
                return;
            }
            logger.info("valid accessToken");
        } else if (accessToken.equals("")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.NO_TOKEN);
            logger.info("no accessToken");
            return;
        } else if (jwtTokenProvider.isValidateToken(accessToken).equals("invalid_token")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.INVALIDE_TOKEN);
            logger.info("invalid accessToken");
            return;
        } else if (jwtTokenProvider.isValidateToken(accessToken).equals("expired_token")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.EXPIRED_TOKEN);
            logger.info("expired accessToken");
            return;
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
