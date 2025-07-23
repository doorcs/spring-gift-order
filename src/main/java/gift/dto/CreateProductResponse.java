package gift.dto;

import java.util.List;

import gift.domain.Product;

public record CreateProductResponse(
    Long id,
    String name,
    Long price,
    String imageUrl,
    List<OptionResponse> options
) {

    public static CreateProductResponse from(Product product) {
        return new CreateProductResponse(
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
