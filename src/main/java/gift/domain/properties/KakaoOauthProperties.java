package gift.domain.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.oauth2.kakao")
public record KakaoOauthProperties(
    String clientId,
    String clientUri,
    String redirectUri,
    String tokenUri,
    String infoUri
) {
}
