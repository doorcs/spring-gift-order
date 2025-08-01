package gift.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import gift.domain.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").update();
    }

    @Test
    void saveTest() {
        // given
        Product product = new Product("상품1", 1000L, "image1", List.of());

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertAll(
            () -> assertThat(savedProduct.getId()).isEqualTo(1L),
            () -> assertThat(savedProduct.getProductName()).isEqualTo("상품1"),
            () -> assertThat(savedProduct.getPrice()).isEqualTo(1000L),
            () -> assertThat(savedProduct.getImageUrl()).isEqualTo("image1")
        );
    }

    @Test
    void findByIdTest() {
        // given
        productRepository.save(new Product("상품1", 1000L, "image1", List.of()));
        productRepository.save(new Product("상품2", 2000L, "image2", List.of()));

        // when
        Optional<Product> product1 = productRepository.findById(1L);
        Optional<Product> product2 = productRepository.findById(2L);

        // then
        assertAll(
            () -> assertThat(product1).isPresent(),
            () -> assertThat(product1.get().getProductName()).isEqualTo("상품1"),
            () -> assertThat(product1.get().getPrice()).isEqualTo(1000L),
            () -> assertThat(product1.get().getImageUrl()).isEqualTo("image1"),
            () -> assertThat(product2).isPresent(),
            () -> assertThat(product2.get().getProductName()).isEqualTo("상품2"),
            () -> assertThat(product2.get().getPrice()).isEqualTo(2000L),
            () -> assertThat(product2.get().getImageUrl()).isEqualTo("image2")
        );
    }

    @Test
    void findByIdFailTest() {
        // given
        productRepository.save(new Product("상품1", 1000L, "image1", List.of()));
        productRepository.save(new Product("상품2", 2000L, "image2", List.of()));

        // when
        Optional<Product> product = productRepository.findById(3L);

        // then
        assertThat(product).isEmpty();
    }

    @Test
    void existsByIdTest() {
        // given
        productRepository.save(new Product("상품1", 1000L, "image1", List.of()));

        // when
        boolean exists = productRepository.existsById(1L);

        // then
        assertThat(exists).isEqualTo(true);
    }

    @Test
    void existsByIdFailTest() {
        // given
        productRepository.save(new Product("상품1", 1000L, "image1", List.of()));

        // when
        boolean exists = productRepository.existsById(2L);

        // then
        assertThat(exists).isEqualTo(false);
    }

    @Test
    void deleteByIdTest() {
        // given
        Product product = productRepository.save(new Product("상품1", 1000L, "image1", List.of()));

        // when, then
        assertThatCode(() -> productRepository.deleteById(product.getId()))
            .doesNotThrowAnyException();
    }
}
