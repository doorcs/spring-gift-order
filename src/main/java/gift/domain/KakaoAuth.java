package gift.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import gift.dto.KakaoTokenResponse;

@Entity
public class KakaoAuth {

    @Id
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiresAt;
    private Instant refreshTokenExpiresAt;

    protected KakaoAuth() {}

    public KakaoAuth(
        Long userId,
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt
    ) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public static KakaoAuth of(Long userId, KakaoTokenResponse response) {
        return new KakaoAuth(
            userId,
            response.accessToken(),
            response.refreshToken(),
            Instant.now().plusSeconds(response.expiresIn()),
            Instant.now().plusSeconds(response.refreshTokenExpiresIn())
        );
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public Instant getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }
}
