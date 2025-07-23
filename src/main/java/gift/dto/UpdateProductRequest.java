package gift.dto;

import java.util.List;

import gift.validation.ProductNamePattern;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(

    @NotBlank(message = "상품명이 입력되지 않았습니다.")
    @Size(max = 15, message = "상품명은 최대 15자까지 입력 가능합니다.")
    @ProductNamePattern
    String name,

    @NotNull(message = "상품 가격이 입력되지 않았습니다.")
    @Min(value = 0, message = "상품 가격은 음수가 될 수 없습니다.")
    Long price,

    @NotBlank(message = "상품 이미지가 입력되지 않았습니다.")
    String imageUrl,

    @NotNull(message = "상품 옵션이 입력되지 않았습니다.")
    @Size(min = 1, message = "상품 옵션은 최소 1개 이상 입력되어야 합니다.")
    @Valid List<OptionRequest> options
) {
}
