package gift.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.CreateProductRequest;
import gift.dto.CreateProductResponse;
import gift.dto.OptionResponse;
import gift.dto.ProductResponse;
import gift.dto.UpdateProductRequest;
import gift.dto.UpdateProductResponse;
import gift.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
        @PageableDefault(size = 2) Pageable pageable
    ) {
        List<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(
        @Valid @RequestBody CreateProductRequest request
    ) {
        CreateProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        UpdateProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponse>> getProductOptions(@PathVariable Long id) {
        List<OptionResponse> options = productService.getProductOptions(id);
        return ResponseEntity.status(HttpStatus.OK).body(options);
    }
}
