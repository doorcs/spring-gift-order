package gift.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gift.exception.ProductNotFoundException;

class MemberTest {

    @Test
    @DisplayName("동일한 상품 중복 추가 시 예외 발생")
    void shouldThrowExceptionWhenAddingDuplicateProduct() {
        // given
        Member member = new Member("test@test.com", "password123!");
        Product product = new Product(1L, "상품1", 1000L, "image1", new ArrayList<>());
        member.addToWishlist(product);

        // when, then
        assertThatThrownBy(() -> member.addToWishlist(product))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("동일한 상품이 이미 위시리스트에 존재합니다.");
    }

    @Test
    @DisplayName("위시리스트에서 상품 제거 가능")
    void removeProductFromWishlist() {
        // given
        Member member = new Member("test@test.com", "password123!");
        Product product = new Product(1L, "상품1", 1000L, "image1", new ArrayList<>());
        member.addToWishlist(product);

        // when
        member.removeFromWishlist(product);

        // then
        assertThat(member.getWishlist()).isEmpty();
    }

    @Test
    @DisplayName("위시리스트에 없는 상품 삭제 시도시 예외 발생")
    void shouldThrowExceptionWhenRemovingNonExists() {
        // given
        Member member = new Member("test@test.com", "password123!");
        Product product = new Product(1L, "상품1", 1000L, "image1", new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> member.removeFromWishlist(product))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("위시리스트에 해당 상품이 존재하지 않습니다.");
    }
}
