package gift.interceptor;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import gift.exception.AuthorizationRequiredException;
import gift.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MemberAuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final Set<String> MEMBERS = Set.of("ROLE_USER", "ROLE_ADMIN");

    public MemberAuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        if (request.getMethod().equals("GET")) {
            return true; // GET 요청은 인증 생략
        }
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new AuthorizationRequiredException("인증이 필요한 요청입니다.");
        }

        String role = tokenProvider.getRole(token.substring(7));
        if (!MEMBERS.contains(role)) {
            throw new AuthorizationRequiredException("인증이 필요한 요청입니다.");
        }

        return true;
    }
}
