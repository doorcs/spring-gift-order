package gift.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishItem;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("ALTER TABLE wish ALTER COLUMN id RESTART WITH 1").update();
        jdbcClient.sql("ALTER TABLE member ALTER COLUMN id RESTART WITH 1").update();
        jdbcClient.sql("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").update();
    }

    @Test
    void saveTest() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        Member member1 = memberRepository.save(new Member(null, "test@test.com", "dbPassword", "ROLE_USER"));
        Product product1 = productRepository.save(new Product(null, "product1", 1000L, "image1", List.of()));

        // when
        wishRepository.save(new Wish(member1, product1));

        // then
        List<WishItem> wishItems = wishRepository.findAllProductByMemberId(1L, pageable);
        assertThat(wishItems).hasSize(1);
        assertThat(wishItems.get(0).productId()).isEqualTo(1L);
        assertThat(wishItems.get(0).name()).isEqualTo("product1");
        assertThat(wishItems.get(0).price()).isEqualTo(1000L);
        assertThat(wishItems.get(0).imageUrl()).isEqualTo("image1");
    }

    @Test
    void findAllProductByMemberIdTest() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        Member member1 = memberRepository.save(new Member(null, "test@test.com", "dbPassword", "ROLE_USER"));
        Product product1 = productRepository.save(new Product(null, "product1", 1000L, "image1", List.of()));
        Product product2 = productRepository.save(new Product(null, "product2", 2000L, "image2", List.of()));
        wishRepository.save(new Wish(member1, product1));
        wishRepository.save(new Wish(member1, product2));

        // when
        List<WishItem> wishItems = wishRepository.findAllProductByMemberId(1L, pageable);

        // then
        assertThat(wishItems).hasSize(2);
        assertThat(wishItems.get(0).productId()).isEqualTo(1L);
        assertThat(wishItems.get(0).name()).isEqualTo("product1");
        assertThat(wishItems.get(0).price()).isEqualTo(1000L);
        assertThat(wishItems.get(0).imageUrl()).isEqualTo("image1");
        assertThat(wishItems.get(1).productId()).isEqualTo(2L);
        assertThat(wishItems.get(1).name()).isEqualTo("product2");
        assertThat(wishItems.get(1).price()).isEqualTo(2000L);
        assertThat(wishItems.get(1).imageUrl()).isEqualTo("image2");
    }
}
