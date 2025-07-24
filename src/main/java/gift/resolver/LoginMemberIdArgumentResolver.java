package gift.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import gift.exception.AuthorizationRequiredException;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;
import io.micrometer.common.util.StringUtils;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public LoginMemberIdArgumentResolver(
        TokenProvider tokenProvider,
        MemberRepository memberRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new AuthorizationRequiredException("인증이 필요한 요청입니다.");
        }

        Long memberId = tokenProvider.getMemberId(token.substring(7));
        if (memberId == null || !memberRepository.existsById(memberId)) {
            throw new AuthorizationRequiredException("인증이 필요한 요청입니다.");
        }

        return memberId;
    }
}
