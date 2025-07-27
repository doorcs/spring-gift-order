package gift.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import gift.dto.KakaoOauth2Response;

@Service
public class Oauth2Service {

    private final RestClient restClient;

    public Oauth2Service(RestClient restClient) {
        this.restClient = restClient;
    }

    public KakaoOauth2Response oauthLogin(String clientId, String tokenUri, String redirectUri,
        String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        KakaoOauth2Response response = restClient.post()
            .uri(tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(KakaoOauth2Response.class);

        // TODO: 토큰을 어떻게 가공해서 JWT와 함께 사용할지 생각해보기

        return response;
    }
}
