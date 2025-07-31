package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoApiErrorResponse(

    @JsonProperty("code")
    String code,

    @JsonProperty("msg")
    String message
) {
}
