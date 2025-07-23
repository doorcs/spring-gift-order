package gift.domain.embed;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gift.domain.Member;
import gift.domain.Product;

class WishlistTest {

    @Test
    @DisplayName("동일한 상품 중복 추가 시 예외 발생")
    void shouldThrowExceptionWhenAddingDuplicateProduct() {
        // given
        Member member = new Member("test@test.com", "dbPassword");
        Product product = new Product(1L, "상품1", 1000L, "image1", new ArrayList<>());

        member.addToWishlist(product);

        // when, then
        assertThatThrownBy(() -> member.addToWishlist(product))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("동일한 상품이 이미 위시리스트에 존재합니다.");
    }

    @Test
    @DisplayName("위시리스트 상품 제거 후 재추가 가능")
    void shouldAllowReAddingAfterRemoval() {
        // given
        Member member = new Member("test@test.com", "dbPassword");
        Product product = new Product(1L, "상품1", 1000L, "image1", new ArrayList<>());

        // when
        member.addToWishlist(product);
        member.removeFromWishlist(product);

        // then
        assertThatCode(() -> member.addToWishlist(product))
            .doesNotThrowAnyException();
    }
}
