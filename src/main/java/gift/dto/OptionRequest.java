package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionRequest(

    @NotBlank(message = "옵션명이 입력되지 않았습니다.")
    String name,

    @NotNull(message = "옵션 수량이 입력되지 않았습니다.")
    @Min(value = 1, message = "옵션 수량은 1개 이상이어야 합니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 1억개 미만이어야 합니다.")
    Long quantity
) {

}
