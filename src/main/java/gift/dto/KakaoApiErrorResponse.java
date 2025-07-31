package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoApiErrorResponse(

    @JsonProperty("code")
    Integer code,

    @JsonProperty("msg")
    String message
) {
}
