package gift.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.exception.LoginException;
import gift.exception.WishNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;

@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(
        MemberRepository memberRepository,
        OptionRepository optionRepository,
        OrderRepository orderRepository,
        KakaoMessageService kakaoMessageService
    ) {
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse order(
        Long memberId,
        OrderRequest request
    ) throws JsonProcessingException {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

        Option option = optionRepository.findById(request.optionId())
            .orElseThrow(() -> new WishNotFoundException("상품을 찾을 수 없습니다."));

        Product product = option.getProduct();

        boolean isProductInWishlist = member.getWishlist()
            .stream()
            .anyMatch(elem -> elem.getProduct().equals(product));

        if (isProductInWishlist) {
            member.removeFromWishlist(product);
        }

        option.subQuantity(request.quantity()); // 수량 유효성 검증 및 예외처리

        Order order = orderRepository.save(new Order(option, member));

        if ("oauth-kakao".equals(member.getPassword())) {
            // 카카오 로그인을 통해 가입한 회원일 경우 메시지 발송 API 호출
            kakaoMessageService.sendMessage(member, request);
        }

        return new OrderResponse(
            order.getId(),
            option.getId(),
            request.quantity(),
            request.message()
        );
    }
}
