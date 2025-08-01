package gift.dto;

public record OrderResponse(

    Long id,
    Long optionId,
    Long quantity,
    String message
) {
}
