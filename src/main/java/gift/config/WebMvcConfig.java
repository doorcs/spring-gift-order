package gift.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gift.interceptor.AdminAuthInterceptor;
import gift.interceptor.MemberAuthInterceptor;
import gift.resolver.LoginMemberIdArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberAuthInterceptor memberAuthInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;
    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;

    public WebMvcConfig(
        MemberAuthInterceptor memberAuthInterceptor,
        AdminAuthInterceptor adminAuthInterceptor,
        LoginMemberIdArgumentResolver loginMemberIdArgumentResolver
    ) {
        this.memberAuthInterceptor = memberAuthInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberAuthInterceptor)
            .addPathPatterns("/api/products/**");

        registry.addInterceptor(adminAuthInterceptor)
            .addPathPatterns("/admin/**"); // `ROLE_ADMIN` 권한의 JWT 토큰을 가진 사용자만 인가
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대한 규칙으로 등록
            .allowedOrigins(
                "http://localhost:3000", // 프론트엔드 개발에서 주로 사용되는 3000번 포트 허용
                "http://ec2-13-125-186-57.ap-northeast-2.compute.amazonaws.com" // 배포 도메인 허용
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
