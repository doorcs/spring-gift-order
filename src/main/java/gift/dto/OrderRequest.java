package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(

    @NotNull(message = "상품 ID가 입력되지 않았습니다.")
    @Min(value = 1, message = "상품 ID가 올바르지 않습니다.")
    Long optionId,

    @NotNull(message = "수량이 입력되지 않았습니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상 입력되어야 합니다.")
    Long quantity,

    @NotBlank(message = "메시지가 입력되지 않았습니다.")
    String message
) {
}
