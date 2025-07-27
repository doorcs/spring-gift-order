package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOauth2Response(

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    int expiresIn,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("refresh_token_expires_in")
    int refreshTokenExpiresIn
) {
}
