package packman.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import packman.util.ResponseCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);  // header에서 가져온 accessToken

        if (jwtTokenProvider.isValidateToken(accessToken).equals("ok")) {  // token 검증
            setAuthentication(accessToken);
        } else if (accessToken.equals("")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.NO_TOKEN);
            return;
        } else if (jwtTokenProvider.isValidateToken(accessToken).equals("invalid_token")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.INVALIDE_TOKEN);
            return;
        } else if (jwtTokenProvider.isValidateToken(accessToken).equals("expired_token")) {
            setErrorResponse((HttpServletResponse) response, ResponseCode.EXPIRED_TOKEN);
            return;
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String jwtToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);  // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication);  // SecurityContextHolder에 인증 객체 저장
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