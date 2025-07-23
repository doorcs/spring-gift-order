package gift.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import gift.exception.AuthorizationRequiredException;
import gift.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public AdminAuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !"ROLE_ADMIN".equals(tokenProvider.getRole(token.substring(7)))) {
            throw new AuthorizationRequiredException("인증이 필요한 요청입니다.");
        }

        return true;
    }
}
