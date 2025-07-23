package gift.dto;

import gift.domain.Option;

public record OptionResponse(Long id, String name, Long quantity) {

    public static OptionResponse from(Option option) {
        return new OptionResponse(
            option.getId(),
            option.getOptionName().getOptionName(),
            option.getQuantity().getQuantity()
        );
    }
}
