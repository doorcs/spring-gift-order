package gift.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gift.domain.KakaoAuth;
import gift.domain.Member;
import gift.dto.OrderRequest;
import gift.exception.LoginException;
import gift.repository.KakaoAuthRepository;

@Service
public class KakaoMessageService {

    @Value("${kakaotalk.api.me}")
    private String messageUri;

    private final KakaoAuthRepository kakaoAuthRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public KakaoMessageService(
        KakaoAuthRepository kakaoAuthRepository,
        ObjectMapper objectMapper,
        RestClient restClient
    ) {
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    @Async
    public void sendMessage(Member member, OrderRequest request) throws JsonProcessingException {
        KakaoAuth kakaoAuth = kakaoAuthRepository.findById(member.getId())
            .orElseThrow(() -> new LoginException(
                "현재 카카오톡 메시지 전송 기능은 카카오 로그인을 통해 가입한 사용자만 지원하고 있습니다."
            ));

        String orderMessage = createOrderMessage(request);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", orderMessage);

        restClient.post()
            .uri(messageUri)
            .header("Authorization", "Bearer %s".formatted(kakaoAuth.getAccessToken()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    private String createOrderMessage(OrderRequest request) throws JsonProcessingException {
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", "주문이 완료되었습니다.\n메시지: %s".formatted(request.message()));
        templateObject.put("link", new HashMap<>());

        return objectMapper.writeValueAsString(templateObject);
    }
}
