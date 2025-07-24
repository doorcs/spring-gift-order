package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class ImageUrl {

    @Column(name = "image_url")
    private String imageUrl;

    protected ImageUrl() {}

    public ImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("상품 이미지가 입력되지 않았습니다.");
        }
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
