package gift.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.KakaoApiErrorResponse;
import gift.dto.KakaoAuthErrorResponse;
import gift.exception.KakaoApiException;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient kakaoApiRestClient(ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));

        return RestClient.builder()
            .requestFactory(requestFactory)
            .defaultStatusHandler(
                statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                (request, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        KakaoApiErrorResponse msg = objectMapper.readValue(
                            response.getBody(), KakaoApiErrorResponse.class
                        );
                        throw new KakaoApiException(
                            "요청 데이터에 오류가 있습니다. 코드: "
                                + msg.code()
                                + ", 메시지: "
                                + msg.message()
                        );
                    } else { // 5xx 에러 핸들링
                        throw new RestClientException("카카오 API 서버 오류가 발생했습니다.");
                    }
                }
            )
            .build();
    }

    @Bean
    public RestClient kakaoAuthRestClient(ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));

        return RestClient.builder()
            .requestFactory(requestFactory)
            .defaultStatusHandler(
                statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                (request, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        KakaoAuthErrorResponse msg = objectMapper.readValue(
                            response.getBody(), KakaoAuthErrorResponse.class
                        );
                        throw new KakaoApiException(
                            "요청 데이터에 오류가 있습니다. 코드: "
                                + msg.error()
                                + ", 메시지: "
                                + msg.errorDescription()
                        );
                    } else { // 5xx 에러 핸들링
                        throw new RestClientException("카카오 API 서버 오류가 발생했습니다.");
                    }
                }
            )
            .build();
    }
}
