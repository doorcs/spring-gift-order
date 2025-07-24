package gift.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.CreateProductRequest;
import gift.dto.CreateProductResponse;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.dto.ProductResponse;
import gift.dto.UpdateProductRequest;
import gift.dto.UpdateProductResponse;
import gift.exception.ApprovalRequiredException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .stream()
            .map(ProductResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return ProductResponse.from(
            productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다.")));
    }

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        validateProductName(request.name());

        Product product = productRepository.save(
            new Product(request.name(), request.price(), request.imageUrl(), new ArrayList<>())
        );
        if (request.options().isEmpty()) {
            throw new IllegalArgumentException("반드시 하나 이상의 옵션이 있어야 합니다.");
        }
        product.addAll(convertToOptionlist(request.options(), product));
        product = productRepository.save(product);

        return CreateProductResponse.from(product);
    }

    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request) {
        checkProductExistence(id);
        validateProductName(request.name());
        Product product = new Product(
            id,
            request.name(),
            request.price(),
            request.imageUrl(),
            new ArrayList<>()
        );
        if (request.options().isEmpty()) {
            throw new IllegalArgumentException("반드시 하나 이상의 옵션이 있어야 합니다.");
        }
        product.addAll(convertToOptionlist(request.options(), product));
        product = productRepository.save(product);

        return UpdateProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        checkProductExistence(id);

        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> getProductOptions(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        return product.getOptions()
            .stream()
            .map(OptionResponse::from)
            .toList();
    }

    @Transactional
    public void subOptionCount(Long optionId, Long quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new ProductNotFoundException("해당 옵션이 존재하지 않습니다."));

        option.subQuantity(quantity);
        optionRepository.save(option);
    }

    private void validateProductName(String productName) {
        if (productName.contains("카카오")) {
            // 추후에 MD 승인이 있을 경우 예외를 발생시키지 않고 return하도록 수정 가능
            throw new ApprovalRequiredException("'카카오'가 포함된 문구를 사용하시려면 담당 MD에게 문의해주세요.");
        }
    }

    private void checkProductExistence(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("해당 상품이 존재하지 않습니다.");
        }
    }

    private List<Option> convertToOptionlist(List<OptionRequest> options, Product product) {
        return options.stream()
            .map(option -> new Option(
                product,
                option.name(),
                option.quantity()
            )).toList();
    }
}
