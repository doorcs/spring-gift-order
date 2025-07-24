package gift.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.AddWishlistRequest;
import gift.dto.WishResponse;
import gift.resolver.LoginMemberId;
import gift.service.WishService;

@RestController
@RequestMapping("/api")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/wishes")
    public ResponseEntity<List<WishResponse>> getProductsFromWishlist(
        @LoginMemberId Long memberId,
        @PageableDefault(size = 2) Pageable pageable
    ) {
        List<WishResponse> products = wishService.getProductsFromWishlist(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/wishes")
    public ResponseEntity<WishResponse> addProductToWishlist(
        @LoginMemberId Long memberId,
        @RequestBody AddWishlistRequest request
    ) {
        WishResponse response = wishService.addProductToWishlist(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/wishes/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(
        @LoginMemberId Long memberId,
        @PathVariable Long productId
    ) {
        wishService.deleteProductFromWishlist(memberId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
