package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAuthErrorResponse(

    @JsonProperty("error")
    String error,

    @JsonProperty("error_description")
    String errorDescription
) {
}
