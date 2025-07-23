package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected Quantity() {}

    public Quantity(Long quantity) {
        if (quantity == null || quantity < 0L || quantity >= 1_000_000_000L) {
            throw new IllegalArgumentException("수량이 입력되지 않았습니다.");
        }

        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
