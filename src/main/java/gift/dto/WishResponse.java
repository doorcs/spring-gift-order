package gift.dto;

public record WishResponse(
    Long productId,
    Long price,
    String name,
    String imageUrl
) {
    public static WishResponse from(WishItem item) {
        return new WishResponse(
            item.productId(),
            item.price(),
            item.name(),
            item.imageUrl()
        );
    }
}
