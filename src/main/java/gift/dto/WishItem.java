package gift.dto;

public record WishItem(
    Long id,
    Long productId,
    Long price,
    String name,
    String imageUrl,
    Long quantity
) {
}
