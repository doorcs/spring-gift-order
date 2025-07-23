package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private Long price;

    protected Price() {}

    public Price(Long price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("상품 가격이 입력되지 않았습니다.");
        }

        this.price = price;
    }

    public Long getPrice() {
        return price;
    }
}
