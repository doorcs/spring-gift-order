package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class ProductName {

    @Column(name = "name", nullable = false)
    private String productName;

    protected ProductName() {}

    public ProductName(String productName) {
        if (!StringUtils.hasText(productName) || productName.length() > 15) {
            throw new IllegalArgumentException("상품명 형식이 올바르지 않습니다.");
        }

        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
