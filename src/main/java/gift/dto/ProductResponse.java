package gift.dto;

import gift.domain.Product;

public record ProductResponse(Long id, String name, Long price, String imageUrl) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getProductName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
