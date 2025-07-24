package gift.dto;

import java.util.List;

import gift.domain.Product;

public record UpdateProductResponse(
    Long id,
    String name,
    Long price,
    String imageUrl,
    List<OptionResponse> options
) {

    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(
            product.getId(),
            product.getProductName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getOptions()
                .stream()
                .map(OptionResponse::from)
                .toList()
        );
    }
}
