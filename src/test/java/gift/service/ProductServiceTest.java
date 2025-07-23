package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.CreateProductRequest;
import gift.dto.CreateProductResponse;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.dto.ProductResponse;
import gift.exception.ApprovalRequiredException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final OptionRepository optionRepository = mock(OptionRepository.class);

    private final ProductService productService= new ProductService(
        productRepository, optionRepository
    );

    @Test
    void getAllProductsTest() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        List<Product> products = List.of(
            new Product(1L, "상품1", 1000L, "image1", List.of()),
            new Product(2L, "상품2", 2000L, "image2", List.of())
        );
        given(productRepository.findAll(pageable)).willReturn(
            new PageImpl<>(products, pageable, products.size())
        );

        // when
        List<ProductResponse> response = productService.getAllProducts(pageable);

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).name()).isEqualTo("상품1");
        assertThat(response.get(0).price()).isEqualTo(1000L);
        assertThat(response.get(0).imageUrl()).isEqualTo("image1");
        assertThat(response.get(1).id()).isEqualTo(2L);
        assertThat(response.get(1).name()).isEqualTo("상품2");
        assertThat(response.get(1).price()).isEqualTo(2000L);
        assertThat(response.get(1).imageUrl()).isEqualTo("image2");
    }

    @Test
    void getProductByIdTest() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.of(
            new Product(productId, "상품1", 1000L, "image", List.of())
        ));

        // when
        ProductResponse response = productService.getProductById(productId);

        // then
        assertThat(response.id()).isEqualTo(productId);
        assertThat(response.name()).isEqualTo("상품1");
        assertThat(response.price()).isEqualTo(1000L);
        assertThat(response.imageUrl()).isEqualTo("image");
    }

    @Test
    void getProductByIdFailTest() {
        // given
        Long failId = 9999L;
        given(productRepository.findById(failId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> productService.getProductById(failId))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void createProductTest() {
        // given
        CreateProductRequest request = new CreateProductRequest("상품1", 1000L, "image1", List.of(new OptionRequest("옵션1", 100L)));
        Product saved = new Product(1L, "상품1", 1000L, "image", new ArrayList<>());
        given(productRepository.save(any())).willReturn(saved);
        given(productRepository.findById(1L)).willReturn(Optional.of(saved));

        // when
        CreateProductResponse response = productService.createProduct(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("상품1");
        assertThat(response.price()).isEqualTo(1000L);
        assertThat(response.imageUrl()).isEqualTo("image");
    }

    @Test
    void createProductFailTest() {
        // given
        CreateProductRequest request = new CreateProductRequest("카카오닙스", 1000L, "image", List.of(new OptionRequest("옵션1", 100L)));

        // when, then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(ApprovalRequiredException.class);
    }
}
