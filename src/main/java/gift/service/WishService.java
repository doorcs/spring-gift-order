package gift.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.Member;
import gift.domain.Product;
import gift.dto.AddWishlistRequest;
import gift.dto.WishResponse;
import gift.exception.MemberNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishService(
        WishRepository wishRepository,
        ProductRepository productRepository,
        MemberRepository memberRepository
    ) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getProductsFromWishlist(Long memberId, Pageable pageable) {
        return wishRepository.findAllProductByMemberId(memberId, pageable)
            .stream()
            .map(WishResponse::from)
            .toList();
    }

    @Transactional
    public WishResponse addProductToWishlist(Long memberId, AddWishlistRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 회원이 존재하지 않습니다."));

        member.addToWishlist(product);
        memberRepository.save(member);

        return new WishResponse(
            product.getId(),
            product.getPrice(),
            product.getProductName(),
            product.getImageUrl()
        );
    }

    @Transactional
    public void deleteProductFromWishlist(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 회원이 존재하지 않습니다."));

        member.removeFromWishlist(product);
        memberRepository.save(member);
    }
}
