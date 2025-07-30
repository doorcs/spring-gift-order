package gift.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gift.domain.KakaoAuth;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.exception.LoginException;
import gift.exception.WishNotFoundException;
import gift.repository.KakaoAuthRepository;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;

@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final KakaoAuthRepository kakaoAuthRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public OrderService(
        MemberRepository memberRepository,
        OptionRepository optionRepository,
        KakaoAuthRepository kakaoAuthRepository,
        OrderRepository orderRepository,
        ObjectMapper objectMapper,
        RestClient restClient
    ) {
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    @Transactional
    public OrderResponse order(
        String messageUri,
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

        if (member.getPassword().equals("oauth-kakao")) {
            // 카카오 로그인을 통해 가입한 회원일 경우 메시지 발송 API 호출
            KakaoAuth kakaoAuth = kakaoAuthRepository.findById(member.getId())
                .orElseThrow(() -> new LoginException("d"));

            Map<String, Object> templateObject = new HashMap<>();
            templateObject.put("object_type", "text");
            templateObject.put("text", "주문이 완료되었습니다.\n" + "메시지: " + request.message());
            templateObject.put("link", new HashMap<>());

            String templateObjectJson = objectMapper.writeValueAsString(templateObject);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", templateObjectJson);

            restClient.post()
                .uri(messageUri)
                .header("Authorization", "Bearer " + kakaoAuth.getAccessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();
        }

        return new OrderResponse(
            order.getId(),
            option.getId(),
            request.quantity(),
            request.message()
        );
    }
}
